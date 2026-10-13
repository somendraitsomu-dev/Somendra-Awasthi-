import React, { useState, useEffect, useMemo } from 'react';
import {
  Search,
  SlidersHorizontal,
  Radio,
  TrendingUp,
  TrendingDown,
  RefreshCw,
  X,
  ChevronDown,
  Briefcase,
  Globe,
  Sliders,
  Sparkles,
  Plus,
  Minus,
  Shield,
  Volume2,
  ArrowUpRight,
  ArrowUpDown,
  Activity,
  Cpu,
  Landmark,
  Rocket,
  Car,
  Repeat,
  Gauge,
  IndianRupee,
  Smartphone,
  ExternalLink,
  CheckCircle2,
  Maximize2,
  Smartphone as PhoneIcon,
  Monitor
} from 'lucide-react';

// Asset and Benchmark Data
interface MarketAsset {
  id: string;
  symbol: string;
  fullSymbol: string;
  tag: string;
  category: string;
  subtitle: string;
  price: number;
  changePercent: number;
  pricePrefix: string;
  decimalPlaces: number;
  iconType: string;
  sparkline: number[];
  high24h: number;
  low24h: number;
  volume: string;
  isDualAction?: boolean;
}

interface Benchmark {
  name: string;
  price: string;
  change: string;
  changePercent: string;
  isPositive: boolean;
  sparkline: number[];
}

interface MarketSession {
  label: string;
  status: 'OPEN' | 'CLOSED' | 'ACTIVE';
  isOpen: boolean;
}

interface Position {
  id: string;
  symbol: string;
  name: string;
  isLong: boolean;
  shares: number;
  entryPrice: number;
  currentPrice: number;
  leverage: number;
}

interface OrderHistory {
  id: string;
  symbol: string;
  isBuy: boolean;
  orderType: string;
  shares: number;
  price: number;
  totalUsd: number;
  time: string;
}

const INITIAL_BENCHMARKS: Benchmark[] = [
  {
    name: 'NIFTY 50',
    price: '24,834.10',
    change: '+207.3',
    changePercent: '+0.84%',
    isPositive: true,
    sparkline: [24600, 24650, 24620, 24710, 24690, 24750, 24780, 24810, 24834]
  },
  {
    name: 'S&P 500',
    price: '5,864.67',
    change: '+64.8',
    changePercent: '+1.12%',
    isPositive: true,
    sparkline: [5800, 5815, 5810, 5835, 5825, 5845, 5850, 5860, 5864]
  },
  {
    name: 'NASDAQ 100',
    price: '20,412.30',
    change: '+291.5',
    changePercent: '+1.45%',
    isPositive: true,
    sparkline: [20100, 20180, 20240, 20290, 20350, 20412]
  },
  {
    name: 'BTC/USD',
    price: '67,420.00',
    change: '+1,865.0',
    changePercent: '+2.85%',
    isPositive: true,
    sparkline: [65500, 66000, 66400, 66200, 66900, 67420]
  }
];

const INITIAL_ASSETS: MarketAsset[] = [
  {
    id: 'NVDA',
    symbol: 'NVDA',
    fullSymbol: 'NVIDIA Corporation',
    tag: 'US TECH',
    category: 'US Tech Giants',
    subtitle: 'NVIDIA Corporation',
    price: 141.20,
    changePercent: 4.25,
    pricePrefix: '$',
    decimalPlaces: 2,
    iconType: 'CHIP',
    sparkline: [134, 135, 137, 138, 140, 141.20],
    high24h: 142.80,
    low24h: 135.50,
    volume: '$48.2B',
    isDualAction: false
  },
  {
    id: 'NIFTY_BASKET',
    symbol: 'NIFT...',
    fullSymbol: 'Nifty 50 Basket',
    tag: 'IND INDEX',
    category: 'Indian / Nifty 50',
    subtitle: 'Nifty Basket · ₹24,834',
    price: 298.50,
    changePercent: 0.84,
    pricePrefix: '$',
    decimalPlaces: 2,
    iconType: 'BANK',
    sparkline: [294, 295, 296, 297, 298.50],
    high24h: 301.20,
    low24h: 294.10,
    volume: '₹18,400 Cr',
    isDualAction: false
  },
  {
    id: 'SPACEX',
    symbol: 'SPAC...',
    fullSymbol: 'Space Exploration Syndicate',
    tag: 'PRE-IPO',
    category: 'Pre-IPO Syndicates',
    subtitle: 'Space Exploration Syn...',
    price: 210.00,
    changePercent: 6.80,
    pricePrefix: '$',
    decimalPlaces: 2,
    iconType: 'ROCKET',
    sparkline: [195, 198, 202, 206, 210.00],
    high24h: 215.00,
    low24h: 194.00,
    volume: '$640M',
    isDualAction: false
  },
  {
    id: 'TSLA',
    symbol: 'TSLA',
    fullSymbol: 'Tesla Incorporated',
    tag: 'NASDAQ',
    category: 'US Tech Giants',
    subtitle: 'Tesla Incorporated',
    price: 238.40,
    changePercent: -1.52,
    pricePrefix: '$',
    decimalPlaces: 2,
    iconType: 'CAR',
    sparkline: [244, 242, 240, 239, 238.40],
    high24h: 245.50,
    low24h: 236.10,
    volume: '$22.4B',
    isDualAction: true
  },
  {
    id: 'TTM',
    symbol: 'TTM',
    fullSymbol: 'Tata Motors Mobility',
    tag: 'ADR / IND',
    category: 'Indian / Nifty 50',
    subtitle: 'Tata Motors Mobility',
    price: 28.40,
    changePercent: 2.15,
    pricePrefix: '$',
    decimalPlaces: 2,
    iconType: 'CAR',
    sparkline: [27.6, 27.9, 28.1, 28.40],
    high24h: 29.10,
    low24h: 27.30,
    volume: '$320M',
    isDualAction: false
  },
  {
    id: 'EURUSD',
    symbol: 'EUR/...',
    fullSymbol: 'Euro / US Dollar',
    tag: 'MAJOR FX',
    category: 'FX Majors',
    subtitle: 'Euro / US Dollar',
    price: 1.0864,
    changePercent: 0.32,
    pricePrefix: '',
    decimalPlaces: 4,
    iconType: 'EXCHANGE',
    sparkline: [1.082, 1.084, 1.085, 1.0864],
    high24h: 1.0890,
    low24h: 1.0815,
    volume: '$2.4T',
    isDualAction: false
  },
  {
    id: 'RB_INDX',
    symbol: 'RB-INDX',
    fullSymbol: 'Red Bull Global Index',
    tag: 'SPORTS',
    category: 'Sports & Indices',
    subtitle: 'Red Bull Global Index',
    price: 88.50,
    changePercent: 3.40,
    pricePrefix: '$',
    decimalPlaces: 2,
    iconType: 'SPEED',
    sparkline: [85, 86.2, 87.1, 88.50],
    high24h: 90.00,
    low24h: 83.50,
    volume: '$140M',
    isDualAction: false
  },
  {
    id: 'USDINR',
    symbol: 'USD/INR',
    fullSymbol: 'US Dollar / Rupee Spot',
    tag: 'FX SPOT',
    category: 'FX Majors',
    subtitle: 'US Dollar / Rupee',
    price: 83.92,
    changePercent: -0.12,
    pricePrefix: '',
    decimalPlaces: 2,
    iconType: 'RUPEE',
    sparkline: [84.10, 84.02, 83.96, 83.92],
    high24h: 84.22,
    low24h: 83.85,
    volume: '$14.8B',
    isDualAction: true
  },
  {
    id: 'AAPL',
    symbol: 'AAPL',
    fullSymbol: 'Apple Incorporated',
    tag: 'MEGA CAP',
    category: 'US Tech Giants',
    subtitle: 'Apple Incorporated',
    price: 231.50,
    changePercent: 0.95,
    pricePrefix: '$',
    decimalPlaces: 2,
    iconType: 'TECH',
    sparkline: [228, 229, 230.5, 231.50],
    high24h: 233.20,
    low24h: 227.80,
    volume: '$34.1B',
    isDualAction: false
  }
];

const SESSIONS: MarketSession[] = [
  { label: 'NYSE', status: 'OPEN', isOpen: true },
  { label: 'NSE INDIA', status: 'CLOSED', isOpen: false },
  { label: 'FX 24/5', status: 'ACTIVE', isOpen: true },
  { label: 'LONDON', status: 'OPEN', isOpen: true },
  { label: 'TOKYO', status: 'CLOSED', isOpen: false }
];

const CATEGORIES = [
  'All Markets (9)',
  '🇮🇳 Indian / Nifty 50',
  '🇺🇸 US Tech Giants',
  '🚀 Pre-IPO Syndicates',
  '💱 FX Majors',
  '🏎️ Sports & Indices'
];

export default function App() {
  const [activeTab, setActiveTab] = useState<'PORTFOLIO' | 'MARKETS' | 'TRADE' | 'ANALYTICS'>('MARKETS');
  const [assets, setAssets] = useState<MarketAsset[]>(INITIAL_ASSETS);
  const [benchmarks, setBenchmarks] = useState<Benchmark[]>(INITIAL_BENCHMARKS);
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedCategory, setSelectedCategory] = useState('All Markets (9)');
  
  // Virtual Capital State
  const [availableCash, setAvailableCash] = useState(8120.00);
  const [positions, setPositions] = useState<Position[]>([
    {
      id: 'pos_1',
      symbol: 'NVDA',
      name: 'NVIDIA Corporation',
      isLong: true,
      shares: 10,
      entryPrice: 135.20,
      currentPrice: 141.20,
      leverage: 1
    },
    {
      id: 'pos_2',
      symbol: 'SPAC...',
      name: 'SpaceX Syndicate',
      isLong: true,
      shares: 5,
      entryPrice: 196.00,
      currentPrice: 210.00,
      leverage: 1
    }
  ]);
  const [orderHistory, setOrderHistory] = useState<OrderHistory[]>([
    {
      id: 'ord_1',
      symbol: 'NVDA',
      isBuy: true,
      orderType: 'MARKET',
      shares: 10,
      price: 135.20,
      totalUsd: 1352.00,
      time: '18m ago'
    },
    {
      id: 'ord_2',
      symbol: 'SPAC...',
      isBuy: true,
      orderType: 'MARKET',
      shares: 5,
      price: 196.00,
      totalUsd: 980.00,
      time: '1h ago'
    }
  ]);
  const [realizedPnl, setRealizedPnl] = useState(342.50);

  // Modals & Slips
  const [selectedAssetForTrade, setSelectedAssetForTrade] = useState<MarketAsset | null>(null);
  const [isQuickTradeOpen, setIsQuickTradeOpen] = useState(false);
  const [isSquawkOpen, setIsSquawkOpen] = useState(false);
  const [isAccountOpen, setIsAccountOpen] = useState(false);
  const [isFilterOpen, setIsFilterOpen] = useState(false);
  const [toastMessage, setToastMessage] = useState<string | null>(null);
  const [isMobileFrameView, setIsMobileFrameView] = useState(false);

  // Trade Desk form state
  const [tradeDeskAsset, setTradeDeskAsset] = useState<MarketAsset>(INITIAL_ASSETS[0]);
  const [tradeDeskSide, setTradeDeskSide] = useState<'BUY' | 'SELL'>('BUY');
  const [tradeDeskUnits, setTradeDeskUnits] = useState(5);
  const [tradeDeskOrderType, setTradeDeskOrderType] = useState('MARKET');
  const [tradeDeskLeverage, setTradeDeskLeverage] = useState(1);
  const [tradeTimeframe, setTradeTimeframe] = useState('1D');

  // Live Tick Simulation
  useEffect(() => {
    const timer = setInterval(() => {
      setAssets((prev) =>
        prev.map((a) => {
          const deltaPct = (Math.random() - 0.48) * 0.35;
          const newPrice = Math.max(0.01, a.price * (1 + deltaPct / 100));
          const newChange = a.changePercent + deltaPct * 0.15;
          const newSpark = [...a.sparkline.slice(1), newPrice];
          return {
            ...a,
            price: newPrice,
            changePercent: newChange,
            sparkline: newSpark
          };
        })
      );
    }, 2500);
    return () => clearInterval(timer);
  }, []);

  // Update positions' current price when assets change
  useEffect(() => {
    setPositions((prev) =>
      prev.map((pos) => {
        const found = assets.find((a) => a.symbol === pos.symbol || a.id === pos.symbol);
        return found ? { ...pos, currentPrice: found.price } : pos;
      })
    );
  }, [assets]);

  // Derived portfolio metrics
  const unrealizedPnl = useMemo(() => {
    return positions.reduce((acc, p) => {
      const diff = p.isLong ? p.currentPrice - p.entryPrice : p.entryPrice - p.currentPrice;
      return acc + diff * p.shares * p.leverage;
    }, 0);
  }, [positions]);

  const totalPortfolioValue = useMemo(() => {
    const posValue = positions.reduce((acc, p) => acc + p.shares * p.currentPrice, 0);
    return availableCash + posValue;
  }, [availableCash, positions]);

  // Filtered Assets
  const filteredAssets = useMemo(() => {
    let list = assets;
    if (searchQuery.trim()) {
      const q = searchQuery.toLowerCase();
      list = list.filter(
        (a) =>
          a.symbol.toLowerCase().includes(q) ||
          a.subtitle.toLowerCase().includes(q) ||
          a.tag.toLowerCase().includes(q) ||
          a.category.toLowerCase().includes(q)
      );
    }
    if (!selectedCategory.startsWith('All Markets')) {
      const cleanCat = selectedCategory.replace(/[^\w\s/]/gi, '').trim();
      list = list.filter((a) => a.category.toLowerCase().includes(cleanCat.toLowerCase()) || cleanCat.toLowerCase().includes(a.category.toLowerCase()));
    }
    return list;
  }, [assets, searchQuery, selectedCategory]);

  const triggerToast = (msg: string) => {
    setToastMessage(msg);
    setTimeout(() => setToastMessage(null), 3500);
  };

  const handleExecuteOrder = (
    asset: MarketAsset,
    isBuy: boolean,
    shares: number,
    price: number,
    orderType: string,
    leverage: number
  ) => {
    const totalCost = (shares * price) / leverage;
    if (isBuy && totalCost > availableCash) {
      triggerToast(`Insufficient SIM Buying Power! Available: $${availableCash.toFixed(2)}`);
      return;
    }

    setAvailableCash((prev) => (isBuy ? prev - totalCost : prev + totalCost));

    // Update or add position
    setPositions((prev) => {
      const idx = prev.findIndex((p) => p.symbol === asset.symbol && p.isLong === isBuy);
      if (idx >= 0) {
        const existing = prev[idx];
        const newShares = existing.shares + shares;
        const newEntry = (existing.entryPrice * existing.shares + price * shares) / newShares;
        const copy = [...prev];
        copy[idx] = { ...existing, shares: newShares, entryPrice: newEntry, currentPrice: price };
        return copy;
      }
      return [
        ...prev,
        {
          id: 'pos_' + Date.now(),
          symbol: asset.symbol,
          name: asset.subtitle,
          isLong: isBuy,
          shares,
          entryPrice: price,
          currentPrice: price,
          leverage
        }
      ];
    });

    // Append order history
    setOrderHistory((prev) => [
      {
        id: 'ord_' + Date.now(),
        symbol: asset.symbol,
        isBuy,
        orderType,
        shares,
        price,
        totalUsd: shares * price,
        time: 'Just now'
      },
      ...prev
    ]);

    setIsQuickTradeOpen(false);
    triggerToast(`Filled: ${isBuy ? 'BUY' : 'SELL'} ${shares} ${asset.symbol} @ $${price.toFixed(2)}`);
  };

  const handleClosePosition = (pos: Position) => {
    const pnl = (pos.isLong ? pos.currentPrice - pos.entryPrice : pos.entryPrice - pos.currentPrice) * pos.shares * pos.leverage;
    const returned = (pos.shares * pos.entryPrice) / pos.leverage + pnl;
    setAvailableCash((c) => c + returned);
    setRealizedPnl((r) => r + pnl);
    setPositions((prev) => prev.filter((p) => p.id !== pos.id));
    setOrderHistory((prev) => [
      {
        id: 'ord_' + Date.now(),
        symbol: pos.symbol,
        isBuy: !pos.isLong,
        orderType: 'MARKET_CLOSE',
        shares: pos.shares,
        price: pos.currentPrice,
        totalUsd: pos.shares * pos.currentPrice,
        time: 'Just now'
      },
      ...prev
    ]);
    triggerToast(`Closed ${pos.symbol} | P&L: ${pnl >= 0 ? '+' : ''}$${pnl.toFixed(2)}`);
  };

  const handleResetBalance = () => {
    setAvailableCash(10000.00);
    setPositions([]);
    setRealizedPnl(0);
    setIsAccountOpen(false);
    triggerToast('Virtual Capital reset to SIM $10,000.00 USD');
  };

  return (
    <div className="min-h-screen bg-[#080B11] text-[#F1F5F9] flex flex-col items-center">
      {/* Top Viewport Mode Switcher for Desktop Prototyping */}
      <div className="w-full bg-[#0F131D] border-b border-[#1B2232] px-4 py-2 flex items-center justify-between text-xs text-[#94A3B8] z-30">
        <div className="flex items-center gap-2">
          <span className="w-2 h-2 rounded-full bg-[#00E599]" />
          <span className="font-semibold text-white tracking-wide">ApexTrade Institutional Telemetry</span>
          <span className="hidden sm:inline text-[#475569]">· Ultra-Low Latency Paper Trading Console</span>
        </div>
        <div className="flex items-center gap-2">
          <button
            onClick={() => setIsMobileFrameView(false)}
            className={`px-2.5 py-1 rounded flex items-center gap-1.5 transition-colors ${!isMobileFrameView ? 'bg-[#00C2FF] text-[#080B11] font-bold' : 'hover:bg-[#1B2232] text-[#94A3B8]'}`}
          >
            <Monitor className="w-3.5 h-3.5" />
            <span>Responsive View</span>
          </button>
          <button
            onClick={() => setIsMobileFrameView(true)}
            className={`px-2.5 py-1 rounded flex items-center gap-1.5 transition-colors ${isMobileFrameView ? 'bg-[#00C2FF] text-[#080B11] font-bold' : 'hover:bg-[#1B2232] text-[#94A3B8]'}`}
          >
            <PhoneIcon className="w-3.5 h-3.5" />
            <span>Device Preview</span>
          </button>
        </div>
      </div>

      {/* Main Terminal Frame */}
      <div
        className={`w-full flex-1 flex flex-col transition-all duration-300 ${
          isMobileFrameView
            ? 'max-w-[440px] my-4 rounded-3xl border border-[#1B2232] shadow-2xl bg-[#080B11] overflow-hidden'
            : 'max-w-4xl'
        }`}
      >
        {/* Terminal Header */}
        <header className="px-4 pt-3 pb-2 flex items-center justify-between border-b border-[#1B2232]/50 bg-[#080B11] sticky top-0 z-20">
          <div className="flex items-center gap-2.5">
            {/* Geometric Apex Logo */}
            <div className="w-9 h-9 rounded-xl bg-gradient-to-br from-[#0F2231] to-[#0B1724] border border-[#00C2FF]/30 flex items-center justify-center shadow-inner">
              <svg viewBox="0 0 24 24" className="w-5 h-5">
                <polygon points="12,3 21,20 3,20" fill="url(#apexGrad)" />
                <defs>
                  <linearGradient id="apexGrad" x1="0" y1="0" x2="0" y2="1">
                    <stop offset="0%" stopColor="#00C2FF" />
                    <stop offset="100%" stopColor="#00E599" />
                  </linearGradient>
                </defs>
              </svg>
            </div>
            <div>
              <div className="flex items-center gap-1.5">
                <span className="font-bold text-lg text-white tracking-tight">ApexTrade</span>
                <span className="inline-flex items-center gap-1 px-1.5 py-0.5 rounded-full bg-[#00E599]/15 border border-[#00E599]/40 text-[#00E599] text-[9px] font-bold tracking-wider">
                  <span className="w-1.5 h-1.5 rounded-full bg-[#00E599] animate-pulse" />
                  LIVE
                </span>
              </div>
              <div
                onClick={() => setIsAccountOpen(true)}
                className="flex items-center gap-1 text-[11px] text-[#94A3B8] cursor-pointer hover:text-white transition-colors"
              >
                <span className="text-[#00C2FF] font-semibold tabular-nums">
                  SIM ${totalPortfolioValue.toLocaleString('en-US', { minimumFractionDigits: 0, maximumFractionDigits: 0 })} USD
                </span>
                <span>•</span>
                <span>Markets</span>
              </div>
            </div>
          </div>

          <div className="flex items-center gap-2">
            <button
              onClick={() => setIsSquawkOpen(true)}
              className="w-9 h-9 rounded-xl bg-[#141A28] border border-[#1B2232] flex items-center justify-center text-[#00C2FF] hover:border-[#00C2FF]/50 transition-colors"
              title="Live Audio Squawk"
            >
              <Radio className="w-4 h-4 animate-pulse" />
            </button>
            <button
              onClick={() => setIsAccountOpen(true)}
              className="relative w-9 h-9 rounded-full bg-[#1E2838] border border-[#1B2232] flex items-center justify-center overflow-hidden hover:border-[#00C2FF] transition-colors"
            >
              <img
                src="https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=120&h=120&q=80"
                alt="Profile"
                className="w-full h-full object-cover"
                referrerPolicy="no-referrer"
              />
              <span className="absolute bottom-0 right-0 w-2.5 h-2.5 rounded-full bg-[#00E599] border-2 border-[#080B11]" />
            </button>
          </div>
        </header>

        {/* Dynamic Tab Body */}
        <main className="flex-1 p-4 pb-24 overflow-y-auto space-y-4">
          {activeTab === 'MARKETS' && (
            <>
              {/* Search Box */}
              <div className="flex items-center gap-2 bg-[#141A28] border border-[#1B2232] rounded-xl px-3 py-1.5 focus-within:border-[#00C2FF] transition-colors">
                <Search className="w-4 h-4 text-[#475569] shrink-0" />
                <input
                  type="text"
                  placeholder="Search 5,000+ US, Nifty 50, Forex & Pre-IPO a..."
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                  className="w-full bg-transparent text-sm text-white placeholder-[#475569] outline-none"
                />
                <button
                  onClick={() => setIsFilterOpen(true)}
                  className="w-7 h-7 rounded-lg bg-[#1E2638] flex items-center justify-center text-[#94A3B8] hover:text-[#00C2FF] transition-colors"
                >
                  <SlidersHorizontal className="w-3.5 h-3.5" />
                </button>
              </div>

              {/* Sessions Bar */}
              <div className="flex items-center gap-2 overflow-x-auto pb-1 no-scrollbar">
                {SESSIONS.map((sess) => (
                  <div
                    key={sess.label}
                    className="inline-flex items-center gap-1.5 px-3 py-1 rounded-full bg-[#141A28] border border-[#1B2232] shrink-0 text-[11px] font-bold"
                  >
                    <span
                      className={`w-1.5 h-1.5 rounded-full ${
                        sess.isOpen ? 'bg-[#00E599]' : 'bg-[#FF3366]'
                      }`}
                    />
                    <span className="text-[#94A3B8]">{sess.label}</span>
                    <span className={sess.isOpen ? 'text-[#00E599]' : 'text-[#FF3366]'}>
                      {sess.status}
                    </span>
                  </div>
                ))}
              </div>

              {/* Global Core Benchmarks */}
              <div>
                <div className="flex items-center justify-between text-[11px] font-bold mb-2 tracking-wider">
                  <div className="flex items-center gap-1.5 text-[#94A3B8]">
                    <Activity className="w-3.5 h-3.5 text-[#00C2FF]" />
                    <span>GLOBAL CORE BENCHMARKS</span>
                  </div>
                  <span className="text-[#475569]">REAL-TIME FEED</span>
                </div>

                <div className="flex items-center gap-3 overflow-x-auto pb-1 no-scrollbar">
                  {benchmarks.map((bm) => (
                    <div
                      key={bm.name}
                      className="min-w-[180px] flex-1 bg-[#141A28] border border-[#1B2232] rounded-2xl p-3.5 shrink-0"
                    >
                      <div className="flex items-center justify-between mb-2">
                        <span className="font-bold text-sm text-white">{bm.name}</span>
                        <span className="px-1.5 py-0.5 rounded bg-[#00E599]/15 text-[#00E599] text-[10px] font-bold">
                          {bm.changePercent}
                        </span>
                      </div>
                      <div className="flex items-baseline justify-between mb-2">
                        <span className="text-lg font-bold tabular-nums text-white">{bm.price}</span>
                        <span className="text-xs font-semibold tabular-nums text-[#00E599]">{bm.change}</span>
                      </div>
                      {/* Sparkline Canvas */}
                      <svg viewBox="0 0 100 24" className="w-full h-7 stroke-[#00E599] fill-none">
                        <path
                          d="M 0,20 Q 25,18 45,12 T 75,8 T 100,2"
                          strokeWidth="2.5"
                          strokeLinecap="round"
                        />
                      </svg>
                    </div>
                  ))}
                </div>
              </div>

              {/* Simulated Flow Radar & Depth Bar */}
              <div className="bg-[#141A28] border border-[#1B2232] rounded-2xl p-4">
                <div className="flex items-center justify-between mb-3">
                  <div className="flex items-center gap-2.5">
                    <div className="w-8 h-8 rounded-lg bg-[#1E273A] flex items-center justify-center text-[#00C2FF]">
                      <Gauge className="w-4 h-4" />
                    </div>
                    <div>
                      <div className="text-[9px] font-bold text-[#475569] tracking-wider">
                        SIMULATED FLOW RADAR
                      </div>
                      <div className="flex items-center gap-1.5 text-xs">
                        <span className="font-bold text-white tabular-nums">68</span>
                        <span className="font-bold text-[#00E599] text-[11px] tracking-wide">
                          GREED / EXPANSION
                        </span>
                      </div>
                    </div>
                  </div>

                  <div className="text-right">
                    <div className="text-[9px] font-bold text-[#475569] tracking-wider">
                      ORDER BOOK DEPTH
                    </div>
                    <div className="text-xs font-semibold text-white tabular-nums">
                      64% Buy · 36% Sell
                    </div>
                  </div>
                </div>

                {/* Split Depth Bar */}
                <div className="w-full h-1.5 rounded-full bg-[#1B2232] overflow-hidden flex mb-2">
                  <div className="h-full bg-[#00E599]" style={{ width: '64%' }} />
                  <div className="h-full bg-[#FF96A3]" style={{ width: '36%' }} />
                </div>

                <div className="flex items-center justify-between text-[11px] text-[#94A3B8]">
                  <div className="flex items-center gap-1.5">
                    <span className="w-1.5 h-1.5 rounded-full bg-[#00E599]" />
                    <span>Bids $42.8M Virtual Vol</span>
                  </div>
                  <div className="flex items-center gap-1.5">
                    <span>Asks $24.1M</span>
                    <span className="w-1.5 h-1.5 rounded-full bg-[#FF96A3]" />
                  </div>
                </div>
              </div>

              {/* Category Filter Pills */}
              <div className="flex items-center gap-2 overflow-x-auto pb-1 no-scrollbar">
                {CATEGORIES.map((cat) => {
                  const isSel = selectedCategory === cat;
                  return (
                    <button
                      key={cat}
                      onClick={() => setSelectedCategory(cat)}
                      className={`px-3.5 py-1.5 rounded-full text-xs font-semibold shrink-0 transition-colors ${
                        isSel
                          ? 'bg-[#00C2FF] text-[#080B11] font-bold'
                          : 'bg-[#141A28] border border-[#1B2232] text-[#94A3B8] hover:text-white'
                      }`}
                    >
                      {cat}
                    </button>
                  );
                })}
              </div>

              {/* Asset List Rows */}
              <div className="space-y-2.5">
                {filteredAssets.map((asset) => {
                  const isGainer = asset.changePercent >= 0;
                  return (
                    <div
                      key={asset.id}
                      onClick={() => {
                        setTradeDeskAsset(asset);
                        setActiveTab('TRADE');
                      }}
                      className="bg-[#141A28] border border-[#1B2232] hover:border-[#00C2FF]/40 rounded-2xl p-3 flex items-center justify-between cursor-pointer transition-all hover:bg-[#171E2F]"
                    >
                      {/* Left: Icon, Ticker, Tag, Name */}
                      <div className="flex items-center gap-3">
                        <div className="w-10 h-10 rounded-xl bg-[#1E2638] flex items-center justify-center text-[#00C2FF] shrink-0">
                          {asset.iconType === 'CHIP' && <Cpu className="w-5 h-5" />}
                          {asset.iconType === 'BANK' && <Landmark className="w-5 h-5" />}
                          {asset.iconType === 'ROCKET' && <Rocket className="w-5 h-5" />}
                          {asset.iconType === 'CAR' && <Car className="w-5 h-5" />}
                          {asset.iconType === 'EXCHANGE' && <Repeat className="w-5 h-5" />}
                          {asset.iconType === 'SPEED' && <Gauge className="w-5 h-5" />}
                          {asset.iconType === 'RUPEE' && <IndianRupee className="w-5 h-5" />}
                          {asset.iconType === 'TECH' && <Smartphone className="w-5 h-5" />}
                        </div>
                        <div>
                          <div className="flex items-center gap-1.5">
                            <span className="font-bold text-white text-base">{asset.symbol}</span>
                            <span className="px-1.5 py-0.5 rounded bg-[#222B3D] text-[#00C2FF] text-[9px] font-bold tracking-wider">
                              {asset.tag}
                            </span>
                          </div>
                          <div className="text-[11px] text-[#94A3B8] truncate max-w-[150px] sm:max-w-[220px]">
                            {asset.subtitle}
                          </div>
                        </div>
                      </div>

                      {/* Right: Price, Change, SIM Button */}
                      <div className="flex items-center gap-3">
                        <div className="text-right">
                          <div className="font-bold text-white text-base tabular-nums">
                            {asset.pricePrefix}
                            {asset.price.toFixed(asset.decimalPlaces)}
                          </div>
                          <div
                            className={`text-xs font-semibold tabular-nums ${
                              isGainer ? 'text-[#00E599]' : 'text-[#FF3366]'
                            }`}
                          >
                            {isGainer ? '+' : ''}
                            {asset.changePercent.toFixed(2)}%
                          </div>
                        </div>

                        {/* Action Button */}
                        <button
                          onClick={(e) => {
                            e.stopPropagation();
                            setSelectedAssetForTrade(asset);
                            setIsQuickTradeOpen(true);
                          }}
                          className={`px-3 py-1.5 rounded-lg flex items-center gap-1 text-xs font-bold transition-transform active:scale-95 ${
                            asset.isDualAction
                              ? 'bg-[#1E2638] border border-[#1B2232] text-[#DFE2F1] hover:bg-[#253046]'
                              : 'bg-[#00C2FF] text-[#080B11] hover:brightness-110'
                          }`}
                        >
                          <span>SIM</span>
                          {asset.isDualAction ? (
                            <ArrowUpDown className="w-3 h-3" />
                          ) : (
                            <ArrowUpRight className="w-3.5 h-3.5" />
                          )}
                        </button>
                      </div>
                    </div>
                  );
                })}
              </div>
            </>
          )}

          {/* TRADE DESK SCREEN */}
          {activeTab === 'TRADE' && (
            <div className="space-y-4">
              {/* Asset Selector Banner */}
              <div className="bg-[#141A28] border border-[#1B2232] rounded-2xl p-4 flex items-center justify-between">
                <div>
                  <div className="flex items-center gap-2">
                    <span className="text-xl font-bold text-white">{tradeDeskAsset.symbol}</span>
                    <span className="px-2 py-0.5 rounded bg-[#222B3D] text-[#00C2FF] text-xs font-bold">
                      {tradeDeskAsset.tag}
                    </span>
                  </div>
                  <div className="text-xs text-[#94A3B8]">{tradeDeskAsset.subtitle}</div>
                </div>
                <div className="text-right">
                  <div className="text-xl font-bold tabular-nums text-white">
                    {tradeDeskAsset.pricePrefix}
                    {tradeDeskAsset.price.toFixed(tradeDeskAsset.decimalPlaces)}
                  </div>
                  <div
                    className={`text-xs font-bold tabular-nums ${
                      tradeDeskAsset.changePercent >= 0 ? 'text-[#00E599]' : 'text-[#FF3366]'
                    }`}
                  >
                    {tradeDeskAsset.changePercent >= 0 ? '+' : ''}
                    {tradeDeskAsset.changePercent.toFixed(2)}%
                  </div>
                </div>
              </div>

              {/* Chart Viewport */}
              <div className="bg-[#141A28] border border-[#1B2232] rounded-2xl p-4">
                <div className="flex items-center justify-between mb-4">
                  <div className="flex items-center gap-1.5">
                    {['1M', '15M', '1H', '1D', '1W'].map((tf) => (
                      <button
                        key={tf}
                        onClick={() => setTradeTimeframe(tf)}
                        className={`px-2.5 py-1 rounded text-xs font-bold ${
                          tradeTimeframe === tf
                            ? 'bg-[#00C2FF] text-[#080B11]'
                            : 'bg-[#1B2232] text-[#94A3B8] hover:text-white'
                        }`}
                      >
                        {tf}
                      </button>
                    ))}
                  </div>
                  <div className="flex items-center gap-1.5 text-xs font-bold text-[#00E599]">
                    <span className="w-2 h-2 rounded-full bg-[#00E599] animate-ping" />
                    <span>Real-Time DMA Stream</span>
                  </div>
                </div>

                {/* SVG Area Chart */}
                <div className="h-36 w-full relative">
                  <svg viewBox="0 0 300 90" className="w-full h-full">
                    <defs>
                      <linearGradient id="tradeGrad" x1="0" y1="0" x2="0" y2="1">
                        <stop offset="0%" stopColor="#00E599" stopOpacity="0.3" />
                        <stop offset="100%" stopColor="#00E599" stopOpacity="0" />
                      </linearGradient>
                    </defs>
                    <path
                      d="M 0,70 Q 50,55 100,65 T 200,30 T 300,15 L 300,90 L 0,90 Z"
                      fill="url(#tradeGrad)"
                    />
                    <path
                      d="M 0,70 Q 50,55 100,65 T 200,30 T 300,15"
                      fill="none"
                      stroke="#00E599"
                      strokeWidth="3"
                      strokeLinecap="round"
                    />
                  </svg>
                </div>
              </div>

              {/* Level 2 Order Book Ladder */}
              <div className="bg-[#141A28] border border-[#1B2232] rounded-2xl p-4">
                <div className="flex items-center justify-between text-xs font-bold mb-2">
                  <span className="text-[#94A3B8]">LEVEL 2 DEPTH LADDER</span>
                  <span className="text-[#00C2FF]">SPREAD 0.02 USD</span>
                </div>
                <div className="space-y-1 text-xs tabular-nums">
                  <div className="flex justify-between text-[#475569] font-bold text-[10px]">
                    <span>BIDS (BUY)</span>
                    <span>PRICE</span>
                    <span>ASKS (SELL)</span>
                  </div>
                  <div className="flex justify-between text-[#FF3366]">
                    <span className="text-transparent">-</span>
                    <span>{(tradeDeskAsset.price + 0.12).toFixed(2)}</span>
                    <span>420</span>
                  </div>
                  <div className="flex justify-between text-[#FF3366]">
                    <span className="text-transparent">-</span>
                    <span>{(tradeDeskAsset.price + 0.05).toFixed(2)}</span>
                    <span>890</span>
                  </div>
                  <div className="flex justify-between text-white font-bold bg-[#1E2638] px-2 py-0.5 rounded">
                    <span className="text-[#00E599]">1,450</span>
                    <span className="text-[#00C2FF]">{tradeDeskAsset.price.toFixed(2)}</span>
                    <span className="text-[#FF3366]">1,200</span>
                  </div>
                  <div className="flex justify-between text-[#00E599]">
                    <span>680</span>
                    <span>{(tradeDeskAsset.price - 0.06).toFixed(2)}</span>
                    <span className="text-transparent">-</span>
                  </div>
                  <div className="flex justify-between text-[#00E599]">
                    <span>1,120</span>
                    <span>{(tradeDeskAsset.price - 0.14).toFixed(2)}</span>
                    <span className="text-transparent">-</span>
                  </div>
                </div>
              </div>

              {/* Execution Ticket */}
              <div className="bg-[#141A28] border border-[#1B2232] rounded-2xl p-4 space-y-4">
                <div className="grid grid-cols-2 gap-2 p-1 bg-[#080B11] rounded-xl">
                  <button
                    onClick={() => setTradeDeskSide('BUY')}
                    className={`py-2 rounded-lg text-xs font-bold transition-colors ${
                      tradeDeskSide === 'BUY'
                        ? 'bg-[#00E599] text-[#051A12]'
                        : 'text-[#94A3B8] hover:text-white'
                    }`}
                  >
                    BUY / LONG
                  </button>
                  <button
                    onClick={() => setTradeDeskSide('SELL')}
                    className={`py-2 rounded-lg text-xs font-bold transition-colors ${
                      tradeDeskSide === 'SELL'
                        ? 'bg-[#FF3366] text-white'
                        : 'text-[#94A3B8] hover:text-white'
                    }`}
                  >
                    SELL / SHORT
                  </button>
                </div>

                <div className="flex items-center justify-between bg-[#080B11] border border-[#1B2232] rounded-xl p-2 px-3">
                  <button
                    onClick={() => setTradeDeskUnits((u) => Math.max(1, u - 1))}
                    className="w-8 h-8 rounded bg-[#141A28] flex items-center justify-center text-white"
                  >
                    <Minus className="w-4 h-4" />
                  </button>
                  <div className="text-center">
                    <div className="text-xs text-[#475569] font-bold">QUANTITY</div>
                    <div className="text-base font-bold tabular-nums text-white">
                      {tradeDeskUnits} UNITS
                    </div>
                  </div>
                  <button
                    onClick={() => setTradeDeskUnits((u) => u + 1)}
                    className="w-8 h-8 rounded bg-[#141A28] flex items-center justify-center text-white"
                  >
                    <Plus className="w-4 h-4" />
                  </button>
                </div>

                <div className="flex items-center justify-between text-xs">
                  <span className="text-[#94A3B8]">Required Margin:</span>
                  <span className="font-bold tabular-nums text-[#00C2FF]">
                    ${((tradeDeskUnits * tradeDeskAsset.price) / tradeDeskLeverage).toFixed(2)}
                  </span>
                </div>

                <button
                  onClick={() =>
                    handleExecuteOrder(
                      tradeDeskAsset,
                      tradeDeskSide === 'BUY',
                      tradeDeskUnits,
                      tradeDeskAsset.price,
                      tradeDeskOrderType,
                      tradeDeskLeverage
                    )
                  }
                  className={`w-full py-3.5 rounded-xl font-bold text-sm tracking-wide transition-all shadow-lg active:scale-98 ${
                    tradeDeskSide === 'BUY'
                      ? 'bg-[#00E599] text-[#051A12] hover:bg-[#00d68f]'
                      : 'bg-[#FF3366] text-white hover:bg-[#ff245a]'
                  }`}
                >
                  {tradeDeskSide === 'BUY' ? 'EXECUTE SIMULATED BUY' : 'EXECUTE SIMULATED SELL'} ({tradeDeskAsset.symbol})
                </button>
              </div>
            </div>
          )}

          {/* PORTFOLIO SCREEN */}
          {activeTab === 'PORTFOLIO' && (
            <div className="space-y-4">
              {/* Virtual Equity Hero */}
              <div className="bg-[#141A28] border border-[#1B2232] rounded-2xl p-4">
                <div className="flex items-center justify-between mb-1">
                  <span className="text-[10px] font-bold text-[#475569] tracking-wider">
                    SIMULATED EQUITY LEDGER
                  </span>
                  <button
                    onClick={handleResetBalance}
                    className="px-2 py-1 rounded bg-[#00C2FF]/15 text-[#00C2FF] text-xs font-bold flex items-center gap-1 hover:bg-[#00C2FF]/25"
                  >
                    <RefreshCw className="w-3 h-3" />
                    <span>Reset $10K</span>
                  </button>
                </div>
                <div className="text-2xl font-bold tabular-nums text-white mb-3">
                  ${totalPortfolioValue.toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
                </div>

                <div className="grid grid-cols-3 gap-2 pt-3 border-t border-[#1B2232]">
                  <div>
                    <div className="text-[9px] font-bold text-[#475569]">UNREALIZED P&L</div>
                    <div
                      className={`text-xs font-bold tabular-nums ${
                        unrealizedPnl >= 0 ? 'text-[#00E599]' : 'text-[#FF3366]'
                      }`}
                    >
                      {unrealizedPnl >= 0 ? '+' : ''}${unrealizedPnl.toFixed(2)}
                    </div>
                  </div>
                  <div>
                    <div className="text-[9px] font-bold text-[#475569]">REALIZED GAINS</div>
                    <div
                      className={`text-xs font-bold tabular-nums ${
                        realizedPnl >= 0 ? 'text-[#00E599]' : 'text-[#FF3366]'
                      }`}
                    >
                      {realizedPnl >= 0 ? '+' : ''}${realizedPnl.toFixed(2)}
                    </div>
                  </div>
                  <div>
                    <div className="text-[9px] font-bold text-[#475569]">AVAILABLE CASH</div>
                    <div className="text-xs font-bold tabular-nums text-[#00C2FF]">
                      ${availableCash.toFixed(2)}
                    </div>
                  </div>
                </div>
              </div>

              {/* Positions List */}
              <div className="space-y-2">
                <div className="text-xs font-bold text-[#94A3B8]">OPEN POSITIONS ({positions.length})</div>
                {positions.length === 0 ? (
                  <div className="text-center py-8 text-xs text-[#475569] bg-[#141A28] rounded-xl border border-[#1B2232]">
                    No open positions. Place paper orders in Markets or Trade desk.
                  </div>
                ) : (
                  positions.map((pos) => {
                    const diff = pos.isLong
                      ? (pos.currentPrice - pos.entryPrice) * pos.shares
                      : (pos.entryPrice - pos.currentPrice) * pos.shares;
                    const isWin = diff >= 0;
                    return (
                      <div
                        key={pos.id}
                        className="bg-[#141A28] border border-[#1B2232] rounded-xl p-3 flex items-center justify-between"
                      >
                        <div>
                          <div className="flex items-center gap-2">
                            <span className="font-bold text-white text-sm">{pos.symbol}</span>
                            <span
                              className={`px-1.5 py-0.5 rounded text-[9px] font-bold ${
                                pos.isLong ? 'bg-[#00E599]/15 text-[#00E599]' : 'bg-[#FF3366]/15 text-[#FF3366]'
                              }`}
                            >
                              {pos.isLong ? 'LONG' : 'SHORT'}
                            </span>
                            <span className="text-xs text-[#94A3B8]">{pos.shares} units</span>
                          </div>
                          <div className="text-[11px] text-[#475569] tabular-nums mt-0.5">
                            Entry ${pos.entryPrice.toFixed(2)} → Current ${pos.currentPrice.toFixed(2)}
                          </div>
                        </div>

                        <div className="flex items-center gap-3">
                          <div className="text-right">
                            <div
                              className={`text-xs font-bold tabular-nums ${
                                isWin ? 'text-[#00E599]' : 'text-[#FF3366]'
                              }`}
                            >
                              {isWin ? '+' : ''}${diff.toFixed(2)}
                            </div>
                            <div className="text-[10px] text-[#475569]">Unrealized</div>
                          </div>
                          <button
                            onClick={() => handleClosePosition(pos)}
                            className="px-2.5 py-1 rounded bg-[#FF3366]/20 border border-[#FF3366]/40 text-[#FF3366] text-xs font-bold hover:bg-[#FF3366]/30"
                          >
                            Close
                          </button>
                        </div>
                      </div>
                    );
                  })
                )}
              </div>

              {/* Order History */}
              <div className="space-y-2">
                <div className="text-xs font-bold text-[#94A3B8]">ORDER AUDIT LOG</div>
                {orderHistory.map((ord) => (
                  <div
                    key={ord.id}
                    className="bg-[#141A28] border border-[#1B2232] rounded-xl p-2.5 px-3 flex items-center justify-between text-xs"
                  >
                    <div>
                      <div className="flex items-center gap-2">
                        <span className="font-bold text-white">{ord.symbol}</span>
                        <span
                          className={`px-1 py-0.5 rounded text-[9px] font-bold ${
                            ord.isBuy ? 'bg-[#00E599]/15 text-[#00E599]' : 'bg-[#FF3366]/15 text-[#FF3366]'
                          }`}
                        >
                          {ord.isBuy ? 'BUY' : 'SELL'}
                        </span>
                        <span className="text-[#94A3B8]">{ord.shares} units</span>
                      </div>
                      <div className="text-[10px] text-[#475569]">{ord.time}</div>
                    </div>
                    <div className="text-right font-bold tabular-nums text-white">
                      ${ord.totalUsd.toFixed(2)}
                    </div>
                  </div>
                ))}
              </div>
            </div>
          )}

          {/* ANALYTICS SCREEN */}
          {activeTab === 'ANALYTICS' && (
            <div className="space-y-4">
              <div className="bg-[#141A28] border border-[#1B2232] rounded-2xl p-4">
                <div className="text-xs font-bold text-[#94A3B8] mb-2">FLOW RADAR BREAKDOWN</div>
                <div className="flex items-baseline gap-2 mb-2">
                  <span className="text-3xl font-bold text-[#00C2FF] tabular-nums">68</span>
                  <span className="text-sm font-bold text-[#00E599]">GREED & SYSTEMIC EXPANSION</span>
                </div>
                <p className="text-xs text-[#94A3B8] leading-relaxed mb-4">
                  Simulated liquidity depth indicators show institutional accumulation in AI Silicon,
                  defense syndicates, and currency hedges.
                </p>
                <div className="space-y-2 text-xs">
                  <div className="flex justify-between">
                    <span className="text-[#94A3B8]">Whale Net Delta:</span>
                    <span className="text-[#00E599] font-bold">+$18.7M Inflow</span>
                  </div>
                  <div className="flex justify-between">
                    <span className="text-[#94A3B8]">CBOE Volatility (VIX):</span>
                    <span className="text-white font-bold">14.85 (Low Risk)</span>
                  </div>
                  <div className="flex justify-between">
                    <span className="text-[#94A3B8]">India VIX:</span>
                    <span className="text-white font-bold">12.40 (Stable)</span>
                  </div>
                </div>
              </div>

              <div className="bg-[#141A28] border border-[#1B2232] rounded-2xl p-4">
                <div className="text-xs font-bold text-[#94A3B8] mb-3">SECTOR RELATIVE STRENGTH</div>
                <div className="space-y-2.5 text-xs">
                  {[
                    { name: 'US Tech Mega-Caps', val: '+4.25%', w: '85%' },
                    { name: 'Pre-IPO Syndicate Baskets', val: '+6.80%', w: '95%' },
                    { name: 'Sports & Global Indices', val: '+3.40%', w: '70%' },
                    { name: 'Indian Nifty Mobility', val: '+1.15%', w: '55%' },
                    { name: 'FX Majors & Spot', val: '+0.32%', w: '40%' }
                  ].map((s) => (
                    <div key={s.name}>
                      <div className="flex justify-between text-[#94A3B8] mb-1">
                        <span>{s.name}</span>
                        <span className="text-[#00E599] font-bold">{s.val}</span>
                      </div>
                      <div className="h-1.5 w-full bg-[#1B2232] rounded-full overflow-hidden">
                        <div className="h-full bg-[#00E599]" style={{ width: s.w }} />
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            </div>
          )}
        </main>

        {/* Bottom Navigation Bar */}
        <nav className="fixed bottom-0 left-0 right-0 max-w-4xl mx-auto bg-[#080B11]/95 backdrop-blur-md border-t border-[#1B2232] px-6 py-2 flex items-center justify-around z-20">
          <button
            onClick={() => setActiveTab('PORTFOLIO')}
            className={`flex flex-col items-center gap-1 text-[10px] font-bold tracking-wider transition-colors ${
              activeTab === 'PORTFOLIO' ? 'text-[#00C2FF]' : 'text-[#475569] hover:text-[#94A3B8]'
            }`}
          >
            <Briefcase className="w-5 h-5" />
            <span>PORTFOLIO</span>
          </button>

          <button
            onClick={() => setActiveTab('MARKETS')}
            className={`flex flex-col items-center gap-1 text-[10px] font-bold tracking-wider transition-colors ${
              activeTab === 'MARKETS' ? 'text-[#00C2FF]' : 'text-[#475569] hover:text-[#94A3B8]'
            }`}
          >
            <Globe className="w-5 h-5" />
            <span>MARKETS</span>
          </button>

          {/* Centered TRADE CTA Button */}
          <button
            onClick={() => setActiveTab('TRADE')}
            className={`flex flex-col items-center gap-1 text-[10px] font-bold tracking-wider transition-colors ${
              activeTab === 'TRADE' ? 'text-[#00C2FF]' : 'text-[#475569]'
            }`}
          >
            <div
              className={`w-12 h-8 rounded-xl flex items-center justify-center transition-colors ${
                activeTab === 'TRADE' ? 'bg-[#00C2FF] text-[#080B11]' : 'bg-[#141A28] border border-[#1B2232] text-[#00C2FF]'
              }`}
            >
              <Sliders className="w-4 h-4" />
            </div>
            <span>TRADE</span>
          </button>

          <button
            onClick={() => setActiveTab('ANALYTICS')}
            className={`flex flex-col items-center gap-1 text-[10px] font-bold tracking-wider transition-colors ${
              activeTab === 'ANALYTICS' ? 'text-[#00C2FF]' : 'text-[#475569] hover:text-[#94A3B8]'
            }`}
          >
            <Sparkles className="w-5 h-5" />
            <span>ANALYTICS</span>
          </button>
        </nav>
      </div>

      {/* QUICK TRADE DIALOG */}
      {isQuickTradeOpen && selectedAssetForTrade && (
        <div className="fixed inset-0 bg-black/70 backdrop-blur-sm z-50 flex items-center justify-center p-4">
          <div className="w-full max-w-sm bg-[#141A28] border border-[#1B2232] rounded-3xl p-5 space-y-4 shadow-2xl">
            <div className="flex items-center justify-between">
              <div>
                <div className="flex items-center gap-2">
                  <span className="font-bold text-lg text-white">{selectedAssetForTrade.symbol}</span>
                  <span className="px-1.5 py-0.5 rounded bg-[#222B3D] text-[#00C2FF] text-[10px] font-bold">
                    {selectedAssetForTrade.tag}
                  </span>
                </div>
                <div className="text-xs text-[#94A3B8]">{selectedAssetForTrade.subtitle}</div>
              </div>
              <button
                onClick={() => setIsQuickTradeOpen(false)}
                className="w-7 h-7 rounded-full bg-[#1E2638] flex items-center justify-center text-[#94A3B8] hover:text-white"
              >
                <X className="w-4 h-4" />
              </button>
            </div>

            <div className="flex items-baseline justify-between py-2 border-y border-[#1B2232]">
              <span className="text-2xl font-bold tabular-nums text-white">
                {selectedAssetForTrade.pricePrefix}
                {selectedAssetForTrade.price.toFixed(selectedAssetForTrade.decimalPlaces)}
              </span>
              <span
                className={`text-sm font-bold tabular-nums ${
                  selectedAssetForTrade.changePercent >= 0 ? 'text-[#00E599]' : 'text-[#FF3366]'
                }`}
              >
                {selectedAssetForTrade.changePercent >= 0 ? '+' : ''}
                {selectedAssetForTrade.changePercent.toFixed(2)}%
              </span>
            </div>

            <div className="space-y-3">
              <button
                onClick={() =>
                  handleExecuteOrder(
                    selectedAssetForTrade,
                    true,
                    5,
                    selectedAssetForTrade.price,
                    'MARKET',
                    1
                  )
                }
                className="w-full py-3 rounded-xl bg-[#00E599] text-[#051A12] font-bold text-sm hover:brightness-110 active:scale-98 transition-all"
              >
                EXECUTE SIM BUY (5 Units · ${(selectedAssetForTrade.price * 5).toFixed(2)})
              </button>
              <button
                onClick={() =>
                  handleExecuteOrder(
                    selectedAssetForTrade,
                    false,
                    5,
                    selectedAssetForTrade.price,
                    'MARKET',
                    1
                  )
                }
                className="w-full py-3 rounded-xl bg-[#FF3366] text-white font-bold text-sm hover:brightness-110 active:scale-98 transition-all"
              >
                EXECUTE SIM SELL (5 Units · ${(selectedAssetForTrade.price * 5).toFixed(2)})
              </button>
            </div>

            <div className="text-center">
              <button
                onClick={() => {
                  setTradeDeskAsset(selectedAssetForTrade);
                  setIsQuickTradeOpen(false);
                  setActiveTab('TRADE');
                }}
                className="text-xs text-[#00C2FF] font-semibold hover:underline inline-flex items-center gap-1"
              >
                <span>Open in Detailed Execution Desk</span>
                <ArrowUpRight className="w-3 h-3" />
              </button>
            </div>
          </div>
        </div>
      )}

      {/* SQUAWK BROADCAST MODAL */}
      {isSquawkOpen && (
        <div className="fixed inset-0 bg-black/70 backdrop-blur-sm z-50 flex items-center justify-center p-4">
          <div className="w-full max-w-sm bg-[#141A28] border border-[#1B2232] rounded-3xl p-5 space-y-4 shadow-2xl">
            <div className="flex items-center justify-between">
              <div className="flex items-center gap-2 text-[#00C2FF]">
                <Radio className="w-5 h-5 animate-pulse" />
                <span className="font-bold text-white text-base">Institutional Squawk Feed</span>
              </div>
              <button
                onClick={() => setIsSquawkOpen(false)}
                className="w-7 h-7 rounded-full bg-[#1E2638] flex items-center justify-center text-[#94A3B8]"
              >
                <X className="w-4 h-4" />
              </button>
            </div>

            {/* Waveform Animation */}
            <div className="h-10 bg-[#080B11] rounded-xl flex items-center justify-center gap-1 px-4">
              {[12, 24, 18, 30, 14, 28, 16, 22, 10, 26, 14].map((h, i) => (
                <div
                  key={i}
                  className="w-1 bg-[#00C2FF] rounded-full animate-pulse"
                  style={{ height: `${h}px`, animationDelay: `${i * 100}ms` }}
                />
              ))}
            </div>

            <div className="space-y-2 text-xs">
              <div className="p-2.5 rounded-xl bg-[#080B11] border border-[#1B2232]">
                <span className="text-[#00C2FF] font-bold">[10:14:02] MACRO: </span>
                <span className="text-white">FED SWAPS PRICE IN 25BPS RATE CUT PROBABILITY SURGE TO 88%</span>
              </div>
              <div className="p-2.5 rounded-xl bg-[#080B11] border border-[#1B2232]">
                <span className="text-[#00E599] font-bold">[10:12:45] TECH: </span>
                <span className="text-white">NVIDIA DATA CENTER ACCELERATOR ORDER FLOW TOPS RECORD REVENUE</span>
              </div>
              <div className="p-2.5 rounded-xl bg-[#080B11] border border-[#1B2232]">
                <span className="text-[#00E599] font-bold">[10:09:18] INDIA: </span>
                <span className="text-white">NIFTY 50 CROSSES PSYCHOLOGICAL 24,800 RESISTANCE ON FII EXPANSION</span>
              </div>
            </div>
          </div>
        </div>
      )}

      {/* ACCOUNT & RESET MODAL */}
      {isAccountOpen && (
        <div className="fixed inset-0 bg-black/70 backdrop-blur-sm z-50 flex items-center justify-center p-4">
          <div className="w-full max-w-sm bg-[#141A28] border border-[#1B2232] rounded-3xl p-5 space-y-4 shadow-2xl">
            <div className="flex items-center justify-between">
              <div className="flex items-center gap-2">
                <Shield className="w-5 h-5 text-[#00C2FF]" />
                <span className="font-bold text-white text-base">Apex Simulation Desk</span>
              </div>
              <button
                onClick={() => setIsAccountOpen(false)}
                className="w-7 h-7 rounded-full bg-[#1E2638] flex items-center justify-center text-[#94A3B8]"
              >
                <X className="w-4 h-4" />
              </button>
            </div>

            <div className="bg-[#080B11] rounded-2xl p-4 border border-[#1B2232]">
              <div className="text-[10px] font-bold text-[#475569]">VIRTUAL DESK BALANCE</div>
              <div className="text-2xl font-bold tabular-nums text-[#00C2FF]">
                ${totalPortfolioValue.toFixed(2)} USD
              </div>
              <div className="text-xs text-[#94A3B8] mt-1">Available: ${availableCash.toFixed(2)}</div>
            </div>

            <div className="space-y-1.5 text-xs text-[#94A3B8]">
              <div className="flex justify-between">
                <span>Routing:</span>
                <span className="text-white font-semibold">Direct Market Access (Simulated)</span>
              </div>
              <div className="flex justify-between">
                <span>Latency:</span>
                <span className="text-[#00E599] font-semibold">&lt; 1.2ms WebSocket</span>
              </div>
              <div className="flex justify-between">
                <span>Max Leverage:</span>
                <span className="text-white font-semibold">10x Prime Brokerage</span>
              </div>
            </div>

            <button
              onClick={handleResetBalance}
              className="w-full py-3 rounded-xl bg-[#1B2232] border border-[#00C2FF]/40 text-[#00C2FF] font-bold text-xs hover:bg-[#00C2FF]/10 transition-colors flex items-center justify-center gap-2"
            >
              <RefreshCw className="w-3.5 h-3.5" />
              <span>RESET ACCOUNT TO SIM $10,000.00 USD</span>
            </button>
          </div>
        </div>
      )}

      {/* FILTER SHEET MODAL */}
      {isFilterOpen && (
        <div className="fixed inset-0 bg-black/70 backdrop-blur-sm z-50 flex items-center justify-center p-4">
          <div className="w-full max-w-sm bg-[#141A28] border border-[#1B2232] rounded-3xl p-5 space-y-4 shadow-2xl">
            <div className="flex items-center justify-between">
              <span className="font-bold text-white text-base">Filter Screener</span>
              <button
                onClick={() => setIsFilterOpen(false)}
                className="w-7 h-7 rounded-full bg-[#1E2638] flex items-center justify-center text-[#94A3B8]"
              >
                <X className="w-4 h-4" />
              </button>
            </div>

            <div className="space-y-2">
              {CATEGORIES.map((cat) => (
                <button
                  key={cat}
                  onClick={() => {
                    setSelectedCategory(cat);
                    setIsFilterOpen(false);
                  }}
                  className={`w-full text-left px-3.5 py-2.5 rounded-xl text-xs font-semibold transition-colors ${
                    selectedCategory === cat
                      ? 'bg-[#00C2FF] text-[#080B11] font-bold'
                      : 'bg-[#080B11] border border-[#1B2232] text-white hover:border-[#00C2FF]/50'
                  }`}
                >
                  {cat}
                </button>
              ))}
            </div>
          </div>
        </div>
      )}

      {/* TOAST NOTIFICATION */}
      {toastMessage && (
        <div className="fixed top-4 left-1/2 -translate-x-1/2 bg-[#141A28] border border-[#00C2FF] text-[#00E599] text-xs font-bold px-4 py-2.5 rounded-xl shadow-2xl z-50 animate-bounce">
          {toastMessage}
        </div>
      )}
    </div>
  );
}
