#!/bin/bash

# Setup script to ensure CocoaPods is available
# Dynamically detect Ruby installation paths for portability

echo "🔧 Setting up CocoaPods environment..."

# Function to detect Ruby installation
detect_ruby_paths() {
    local ruby_path=""
    local gem_path=""
    local gem_home=""
    local gem_path_var=""
    
    # Try rbenv first
    if command -v rbenv &> /dev/null; then
        echo "📦 Detected rbenv installation"
        ruby_path="$(rbenv which ruby)"
        if [ -n "$ruby_path" ]; then
            ruby_path="$(dirname "$ruby_path")"
            gem_path="$ruby_path/../lib/ruby/gems/$(rbenv version-name | cut -d. -f1-2).0/bin"
            gem_home="$HOME/.gem/ruby/$(rbenv version-name)"
            gem_path_var="$HOME/.gem/ruby/$(rbenv version-name)"
        fi
    # Try rvm next
    elif command -v rvm &> /dev/null; then
        echo "📦 Detected rvm installation"
        ruby_path="$(rvm current do which ruby)"
        if [ -n "$ruby_path" ]; then
            ruby_path="$(dirname "$ruby_path")"
            gem_path="$ruby_path/../lib/ruby/gems/$(rvm current | cut -d. -f1-2).0/bin"
            gem_home="$HOME/.rvm/gems/ruby-$(rvm current)/gems"
            gem_path_var="$HOME/.rvm/gems/ruby-$(rvm current)"
        fi
    # Fall back to system Ruby
    else
        echo "📦 Using system Ruby installation"
        ruby_path="$(dirname "$(which ruby)")"
        gem_path="$ruby_path/../lib/ruby/gems/$(ruby -e 'puts RUBY_VERSION.split(".")[0..1].join(".")').0/bin"
        gem_home="$HOME/.gem/ruby/$(ruby -e 'puts RUBY_VERSION.split(".")[0..1].join(".")')"
        gem_path_var="$HOME/.gem/ruby/$(ruby -e 'puts RUBY_VERSION.split(".")[0..1].join(".")')"
    fi
    
    # Export paths if found
    if [ -n "$ruby_path" ] && [ -d "$ruby_path" ]; then
        export PATH="$ruby_path:$PATH"
        echo "✅ Added Ruby path: $ruby_path"
        
        if [ -d "$gem_path" ]; then
            export PATH="$gem_path:$PATH"
            echo "✅ Added gem path: $gem_path"
        fi
        
        if [ -d "$gem_home" ]; then
            export GEM_HOME="$gem_home"
            echo "✅ Set GEM_HOME: $gem_home"
        fi
        
        if [ -d "$gem_path_var" ]; then
            export GEM_PATH="$gem_path_var"
            echo "✅ Set GEM_PATH: $gem_path_var"
        fi
    else
        echo "⚠️  Could not detect Ruby paths automatically"
        return 1
    fi
}

# Detect and set Ruby paths
if detect_ruby_paths; then
    echo "✅ Ruby environment configured successfully"
else
    echo "❌ Failed to configure Ruby environment"
    exit 1
fi

# Test if CocoaPods is available
if command -v pod &> /dev/null; then
    echo "✅ CocoaPods is available: $(pod --version)"
    exit 0
else
    echo "📦 CocoaPods not found. Installing..."
    gem install cocoapods
    exit $?
fi 