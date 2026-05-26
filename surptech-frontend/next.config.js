/** @type {import('next').NextConfig} */
const nextConfig = {
  reactStrictMode: true,
  poweredByHeader: false,
  compress: true,
  env: {
    DATA_AGGREGATOR_HOST: process.env.DATA_AGGREGATOR_HOST || 'localhost',
    DATA_AGGREGATOR_PORT: process.env.DATA_AGGREGATOR_PORT || '5555',
  },
}

module.exports = nextConfig
