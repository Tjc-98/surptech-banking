'use client';

import { CustomerCreditInfo } from '@/domain/entity/CustomerCreditInfo';
import { Card, CardHeader, CardContent } from '@/components/ui/Card';
import { Icon } from '@/components/ui/Icon';
import { 
  formatCurrency, 
  formatPercentage, 
  formatFullName,
  maskSocialSecurityNumber 
} from '@/utils/format.utils';

interface CustomerInfoCardProps {
  customerInfo: CustomerCreditInfo;
}

export const CustomerInfoCard: React.FC<CustomerInfoCardProps> = ({ customerInfo }) => {
  return (
    <Card>
      <CardHeader 
        title="Customer Banking Information" 
        subtitle="Account Overview and Credit Details" 
      />
      <CardContent>
        <Section title="Personal Information" icon="user">
          <InfoGrid>
            <InfoField 
              label="Full Name" 
              value={formatFullName(customerInfo.firstName, customerInfo.lastName)} 
            />
            <InfoField 
              label="SSN" 
              value={maskSocialSecurityNumber(customerInfo.socialSecurityNumber)} 
            />
            <InfoField 
              label="Address" 
              value={customerInfo.address} 
              fullWidth 
            />
          </InfoGrid>
        </Section>
        
        <Section title="Credit Information" icon="card">
          <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
            <MetricCard
              label="Full Credit Balance"
              value={formatCurrency(customerInfo.fullCreditBalance)}
              icon="💳"
              variant="blue"
            />
            <MetricCard
              label="Available Balance"
              value={formatCurrency(customerInfo.spendBalance)}
              icon="💰"
              variant="green"
            />
            <MetricCard
              label="Interest Rate"
              value={formatPercentage(customerInfo.interestRate)}
              icon="📊"
              variant="purple"
            />
          </div>
        </Section>
      </CardContent>
    </Card>
  );
};

interface SectionProps {
  title: string;
  icon: 'user' | 'card';
  children: React.ReactNode;
}

const Section: React.FC<SectionProps> = ({ title, icon, children }) => (
  <div className="mb-8 last:mb-0">
    <h3 className="text-lg font-semibold text-secondary-800 mb-4 flex items-center gap-2">
      <Icon name={icon} className="w-5 h-5 text-primary-600" />
      {title}
    </h3>
    {children}
  </div>
);

const InfoGrid: React.FC<{ children: React.ReactNode }> = ({ children }) => (
  <div className="grid grid-cols-1 md:grid-cols-2 gap-6">{children}</div>
);

interface InfoFieldProps {
  label: string;
  value: string;
  fullWidth?: boolean;
}

const InfoField: React.FC<InfoFieldProps> = ({ label, value, fullWidth }) => (
  <div className={fullWidth ? 'md:col-span-2' : ''}>
    <p className="text-sm font-medium text-secondary-500 mb-1">{label}</p>
    <p className="text-base text-secondary-900 font-medium">{value}</p>
  </div>
);

interface MetricCardProps {
  label: string;
  value: string;
  icon: string;
  variant: 'blue' | 'green' | 'purple';
}

const VARIANT_STYLES = {
  blue: 'bg-blue-50 border-blue-200',
  green: 'bg-green-50 border-green-200',
  purple: 'bg-purple-50 border-purple-200',
} as const;

const MetricCard: React.FC<MetricCardProps> = ({ label, value, icon, variant }) => (
  <div className={`${VARIANT_STYLES[variant]} border-2 rounded-lg p-4 text-center`}>
    <div className="text-3xl mb-2">{icon}</div>
    <p className="text-sm font-medium text-secondary-600 mb-1">{label}</p>
    <p className="text-xl font-bold text-secondary-900">{value}</p>
  </div>
);
