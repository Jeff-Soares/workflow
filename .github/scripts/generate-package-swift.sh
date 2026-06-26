#!/usr/bin/env bash
set -euo pipefail

# Gera o Package.swift do repositório mobile-shared.
#
# Uso:  generate-package-swift.sh <output-path>
# Env:  VERSION   versão a publicar (ex.: 0.0.2)
#       CHECKSUM  SHA-256 do MobileShared-<VERSION>.xcframework.zip

OUT="$1"
URL="https://android-s3-tests.s3.sa-east-1.amazonaws.com/MobileShared-${VERSION}.xcframework.zip"

cat > "$OUT" <<EOF
// swift-tools-version:6.1
import PackageDescription

let package = Package(
    name: "MobileShared",
    platforms: [
        .iOS(.v14)
    ],
    products: [
        .library(
            name: "MobileShared",
            targets: ["MobileShared"]
        )
    ],
    targets: [
        .binaryTarget(
            name: "MobileShared",
            url: "${URL}",
            checksum: "${CHECKSUM}"
        )
    ]
)
EOF
