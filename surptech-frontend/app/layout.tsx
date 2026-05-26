import type { Metadata } from 'next';
import { Inter } from 'next/font/google';
import { Icon } from '@/components/ui/Icon';
import './globals.css';

const inter = Inter({ subsets: ['latin'] });

export const metadata: Metadata = {
  title: 'SurpTech Banking System',
  description: 'Customer banking information portal for SurpTech Banking System',
  keywords: ['banking', 'customer portal', 'credit information'],
};

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="en">
      <body className={inter.className}>
        <div className="min-h-screen bg-gradient-to-br from-secondary-50 via-primary-50 to-secondary-100 flex flex-col">
          <Header />
          <main className="flex-1 max-w-7xl w-full mx-auto px-4 sm:px-6 lg:px-8 py-12">
            {children}
          </main>
          <Footer />
        </div>
      </body>
    </html>
  );
}

const Header = () => (
  <header className="bg-white shadow-md">
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
      <div className="flex items-center justify-between">
        <Logo />
        <StatusIndicator />
      </div>
    </div>
  </header>
);

const Logo = () => (
  <div className="flex items-center gap-3">
    <div className="w-10 h-10 bg-gradient-to-br from-primary-600 to-primary-700 rounded-lg flex items-center justify-center">
      <span className="text-white font-bold text-xl">S</span>
    </div>
    <div>
      <h1 className="text-2xl font-bold text-secondary-900">SurpTech Banking</h1>
      <p className="text-sm text-secondary-600">Customer Information Portal</p>
    </div>
  </div>
);

const StatusIndicator = () => (
  <div className="hidden md:flex items-center gap-2 text-sm text-secondary-600">
    <Icon name="check" className="w-5 h-5 text-green-500" />
    <span>System Online</span>
  </div>
);

const Footer = () => (
  <footer className="bg-white border-t border-secondary-200">
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
      <div className="flex flex-col md:flex-row justify-between items-center gap-4">
        <p className="text-sm text-secondary-600">
          © 2024 SurpTech Banking System. All rights reserved.
        </p>
        <nav className="flex items-center gap-6 text-sm text-secondary-600">
          <a href="#" className="hover:text-primary-600 transition-colors">Privacy Policy</a>
          <a href="#" className="hover:text-primary-600 transition-colors">Terms of Service</a>
          <a href="#" className="hover:text-primary-600 transition-colors">Support</a>
        </nav>
      </div>
    </div>
  </footer>
);
