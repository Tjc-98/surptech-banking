'use client';

import { SearchForm } from '@/components/SearchForm';
import { CustomerInfoCard } from '@/components/CustomerInfoCard';
import { LoadingSpinner } from '@/components/LoadingSpinner';
import { ErrorMessage } from '@/components/ErrorMessage';
import { Card } from '@/components/ui/Card';
import { useCustomerCreditInfo } from '@/hooks/useCustomerCreditInfo';

export default function HomePage() {
  const { data, loading, error, fetchCustomerInfo, reset } = useCustomerCreditInfo();
  
  return (
    <div className="flex flex-col items-center space-y-8">
      <Hero />
      <SearchForm onSearch={fetchCustomerInfo} disabled={loading} />
      <ResultsSection data={data} loading={loading} error={error} onDismissError={reset} />
      <InfoBanner />
    </div>
  );
}

const Hero = () => (
  <div className="text-center max-w-2xl">
    <h2 className="text-4xl font-bold text-secondary-900 mb-4">
      Customer Information Lookup
    </h2>
    <p className="text-lg text-secondary-600">
      Enter a Social Security Number to view comprehensive customer banking information
      including personal details and credit profile.
    </p>
  </div>
);

interface ResultsSectionProps {
  data: any;
  loading: boolean;
  error: any;
  onDismissError: () => void;
}

const ResultsSection: React.FC<ResultsSectionProps> = ({ data, loading, error, onDismissError }) => {
  if (loading) return <LoadingSpinner />;
  if (error) return <ErrorMessage error={error} onDismiss={onDismissError} />;
  if (data) return <CustomerInfoCard customerInfo={data} />;
  return <EmptyState />;
};

const EmptyState = () => (
  <Card className="p-12 text-center">
    <div className="text-6xl mb-4">🔍</div>
    <h3 className="text-xl font-semibold text-secondary-800 mb-2">Ready to Search</h3>
    <p className="text-secondary-600 mb-8">Enter a Social Security Number above to get started</p>
    <div className="grid grid-cols-1 md:grid-cols-3 gap-6 text-left">
      <Feature icon="👤" title="Personal Info" description="View customer name and address details" />
      <Feature icon="💳" title="Credit Balance" description="Check full credit balance and limits" />
      <Feature icon="📊" title="Interest Rates" description="Review current interest rate information" />
    </div>
  </Card>
);

interface FeatureProps {
  icon: string;
  title: string;
  description: string;
}

const Feature: React.FC<FeatureProps> = ({ icon, title, description }) => (
  <div className="flex flex-col items-center text-center p-4">
    <div className="text-4xl mb-3">{icon}</div>
    <h4 className="font-semibold text-secondary-800 mb-2">{title}</h4>
    <p className="text-sm text-secondary-600">{description}</p>
  </div>
);

const InfoBanner = () => (
  <div className="w-full max-w-4xl">
    <div className="bg-primary-50 border-2 border-primary-200 rounded-lg p-6">
      <div className="flex items-start gap-3">
        <div className="flex-shrink-0 text-2xl">ℹ️</div>
        <div>
          <h4 className="font-semibold text-primary-900 mb-2">System Information</h4>
          <p className="text-sm text-primary-800">
            This portal connects to the SurpTech Banking data aggregator service 
            to retrieve customer information. Ensure the backend services are running 
            on the configured ports (default: localhost:5555).
          </p>
        </div>
      </div>
    </div>
  </div>
);
