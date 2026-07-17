#!/usr/bin/env bash
set -euo pipefail

if [ $# -ne 2 ]; then
  echo "Usage: $0 <dependency> <X.Y.Z>"
  echo "Example: $0 apolloCache 1.0.8"
  exit 1
fi

dependency="$1"
version="$2"

find . -name 'libs.versions.toml' \
  -exec sed -E -i '' \
    "s/^(${dependency}[[:space:]]*=[[:space:]]*\").*(\")$/\1${version}\2/" \
    {} +
