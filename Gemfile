source "https://rubygems.org"

gem "fastlane"

plugins_path = File.join(File.dirname(__FILE__), 'fastlane', 'Pluginfile')
eval_gemfile(plugins_path) if File.exist?(plugins_path)

# Since ruby 3.4.0 these are not included in the standard library
gem 'abbrev'
gem 'logger'
gem 'mutex_m'
gem 'csv'

# Starting with Ruby 3.5.0, these are not included in the standard library
gem 'ostruct'
