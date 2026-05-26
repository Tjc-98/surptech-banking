'use client';

import { Card } from '@/components/ui/Card';

interface LoadingSpinnerProps {
  message?: string;
}

export const LoadingSpinner: React.FC<LoadingSpinnerProps> = ({ 
  message = 'Loading customer information...' 
}) => (
  <Card className="p-12">
    <div className="flex flex-col items-center justify-center">
      <div className="relative">
        <div className="w-20 h-20 border-4 border-primary-200 rounded-full animate-spin border-t-primary-600" />
        <div className="absolute inset-0 flex items-center justify-center">
          <div className="w-12 h-12 bg-primary-100 rounded-full animate-pulse" />
        </div>
      </div>
      
      <p className="mt-6 text-secondary-600 font-medium text-center">{message}</p>
      
      <div className="mt-4 flex gap-2">
        {[0, 150, 300].map((delay) => (
          <div 
            key={delay}
            className="w-2 h-2 bg-primary-600 rounded-full animate-bounce" 
            style={{ animationDelay: `${delay}ms` }} 
          />
        ))}
      </div>
    </div>
  </Card>
);
