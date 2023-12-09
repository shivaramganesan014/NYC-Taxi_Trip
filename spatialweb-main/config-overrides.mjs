// config-overrides.mjs
import webpack from 'webpack';

export default function override(config, env) {
  // Add fallbacks for node.js core modules
  config.resolve.fallback = {
    ...config.resolve.fallback,
    url: require.resolve('url/'),
  };

  return config;
}
