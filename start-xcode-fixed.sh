#!/bin/bash

echo "🚀 Starting Xcode with complete setup..."

# Detect Ruby installation paths dynamically
RUBY_PATH=""
GEM_PATH=""

# Check for rbenv
if command -v rbenv &> /dev/null; then
    RUBY_PATH="$(rbenv prefix)/bin"
    GEM_PATH="$(rbenv prefix)/lib/ruby/gems/*/bin"
    echo "✅ Found rbenv installation"
# Check for rvm
elif command -v rvm &> /dev/null; then
    RUBY_PATH="$(rvm current do which ruby | xargs dirname)"
    GEM_PATH="$(rvm current do gem env gempath | tr ':' '\n' | head -1)/bin"
    echo "✅ Found rvm installation"
# Check for system Ruby
elif command -v ruby &> /dev/null; then
    RUBY_PATH="$(which ruby | xargs dirname)"
    GEM_PATH="$(gem env gempath | tr ':' '\n' | head -1)/bin"
    echo "✅ Found system Ruby installation"
else
    echo "❌ No Ruby installation found. Please install Ruby first."
    echo "💡 Recommended: Use rbenv, rvm, or system Ruby"
    exit 1
fi

# Build PATH with common locations
COMMON_PATHS="/opt/homebrew/bin:/opt/homebrew/sbin:/usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin"
FULL_PATH="$RUBY_PATH:$GEM_PATH:$COMMON_PATHS:$PATH"

echo "📍 Ruby path: $RUBY_PATH"
echo "📍 Gem path: $GEM_PATH"
echo "🔧 Setting PATH: $FULL_PATH"

# Set environment variables
export PATH="$FULL_PATH"

# Verify CocoaPods is available
if ! command -v pod &> /dev/null; then
    echo "❌ ERROR: CocoaPods executable not found in your PATH."
    echo "Current PATH: $PATH"
    exit 1
fi

echo "✅ CocoaPods found: $(pod --version)"

# Close Xcode if it's running
echo "🔄 Closing Xcode..."
pkill -f "Xcode" || true
sleep 2

# Generate the dummy framework
echo "🔨 Generating dummy framework..."
./gradlew :composeApp:generateDummyFramework

# Install pods
echo "📦 Installing CocoaPods dependencies..."
cd iosApp && pod install && cd ..

echo "✅ Setup complete!"
echo "📱 Opening Xcode workspace..."

# Open Xcode with the workspace
open iosApp/iosApp.xcworkspace

echo "✅ Xcode should now work without CocoaPods errors!"
echo "💡 If you still get errors, try cleaning the build folder in Xcode" 