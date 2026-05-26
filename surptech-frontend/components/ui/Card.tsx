'use client';

interface CardProps {
  children: React.ReactNode;
  className?: string;
}

export const Card: React.FC<CardProps> = ({ children, className = '' }) => (
  <div className={`w-full max-w-4xl bg-white rounded-xl shadow-xl ${className}`}>
    {children}
  </div>
);

interface CardHeaderProps {
  title: string;
  subtitle?: string;
}

export const CardHeader: React.FC<CardHeaderProps> = ({ title, subtitle }) => (
  <div className="bg-gradient-to-r from-primary-600 to-primary-700 px-8 py-6">
    <h2 className="text-2xl font-bold text-white mb-2">{title}</h2>
    {subtitle && <p className="text-primary-100 text-sm">{subtitle}</p>}
  </div>
);

interface CardContentProps {
  children: React.ReactNode;
}

export const CardContent: React.FC<CardContentProps> = ({ children }) => (
  <div className="p-8">{children}</div>
);
