#!/bin/bash

echo "🔧 Setting up environment variables for Xcode..."

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
FULL_PATH="$RUBY_PATH:$GEM_PATH:$COMMON_PATHS"

echo "📍 Ruby path: $RUBY_PATH"
echo "📍 Gem path: $GEM_PATH"
echo "🔧 Setting PATH: $FULL_PATH"

# Create a launch agent to set environment variables for Xcode
cat > ~/Library/LaunchAgents/com.localpk.xcode-cocoapods.plist << EOF
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
<dict>
    <key>Label</key>
    <string>com.localpk.xcode-cocoapods</string>
    <key>ProgramArguments</key>
    <array>
        <string>/bin/launchctl</string>
        <string>setenv</string>
        <string>PATH</string>
        <string>$FULL_PATH</string>
    </array>
    <key>RunAtLoad</key>
    <true/>
</dict>
</plist>
EOF

# Load the launch agent
launchctl load ~/Library/LaunchAgents/com.localpk.xcode-cocoapods.plist

# Set environment variables for current session
launchctl setenv PATH "$FULL_PATH"

echo "✅ Environment variables set for Xcode"
echo "🔄 Please restart Xcode completely for changes to take effect"
echo "💡 You can now open Xcode normally: open iosApp/iosApp.xcworkspace"
echo ""
echo "📝 Note: This script detected your Ruby installation automatically."
echo "   If you have issues, you may need to install Ruby or CocoaPods first:"
echo "   - Install Ruby: https://www.ruby-lang.org/en/documentation/installation/"
echo "   - Install CocoaPods: gem install cocoapods" 