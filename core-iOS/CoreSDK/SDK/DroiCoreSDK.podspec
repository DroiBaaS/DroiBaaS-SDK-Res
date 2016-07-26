Pod::Spec.new do |s|

  s.name         = "DroiCoreSDK"
  s.version      = "0.5.1756"
  s.summary      = "This CoreSDK is one part of DroiBaaS for iOS"
  s.homepage     = "https://github.com/DroiBaaS/DroiBaaS-Core-iOS"
  s.license      = { :type => "Apache License, Version 2.0", :file => "LICENSE" }

  s.author             = { "Jerry Chan" => "jerry.chan@droi.com.tw" }
  s.platform     = :ios, "8.0"
  s.source       = { :git => "https://github.com/DroiBaaS/DroiBaaS-Core-iOS.git", :tag => s.version.to_s }
  s.vendored_frameworks = "**/DroiCoreSDK.framework"
  s.frameworks = "CFNetwork"
  s.libraries = "z", "sqlite3", "objc"
  s.preserve_paths = "scripts/*", "sources/*"
  s.requires_arc = true
end
