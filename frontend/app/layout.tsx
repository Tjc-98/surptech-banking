import type { Metadata } from 'next';

export const metadata: Metadata = {
  title: 'SurpTech Banking',
  description: 'Look up customer accounts, track credit balances, and record transactions.',
};

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="en">
      <body style={{ fontFamily: 'Arial, sans-serif', backgroundColor: '#f5f5f5', margin: 0 }}>
        {children}
      </body>
    </html>
  );
}
