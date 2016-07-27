Pod::Spec.new do |s|
s.name             = "DroiFeedbackSDK"
s.version          = "0.0.8"
s.summary          = "DroiFeedbackSDK iOS 卓易反馈"
s.description      = "DroiFeedbackSDK iOS 卓易反馈，使得开发者可以快速集成反馈功能"
s.homepage         = "https://github.com/DroiBaaS"
# s.screenshots      = "www.example.com/screenshots_1", "www.example.com/screenshots_2"
s.license          = 'MIT'
s.author           = { "DoriBaaS" => "droiinfo@droi.com" }
s.source           = { :git => "https://github.com/DroiBaaS/DroiBaaS-SDK-Res.git", :tag => s.version.to_s }
# s.social_media_url = 'https://twitter.com/NAME'

s.platform     = :ios, '8.0'

s.requires_arc = true

s.vendored_frameworks = "**/DroiFeedback.framework"
s.resources = "**/DroiFeedback.framework/*.{png,xib,nib,bundle}"
s.frameworks = 'Foundation', 'UIKit'
s.libraries = "sqlite3"
s.dependency 'DroiCoreSDK'
end