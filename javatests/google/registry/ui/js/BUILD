package(
    default_testonly = 1,
    default_visibility = ["//java/google/registry:registry_project"],
)

licenses(["notice"])  # Apache 2.0

load("@io_bazel_rules_closure//closure:defs.bzl", "closure_js_library", "closure_js_test")


closure_js_library(
    name = "testing",
    srcs = ["testing.js"],
    deps = [
        "//javascript/closure",
        "//javascript/closure:testing",
    ],
)

closure_js_test(
    name = "test",
    size = "medium",
    timeout = "short",
    srcs = glob(["*_test.js"]),
    compilation_level = "WHITESPACE_ONLY",
    deps = [
        ":testing",
        "//javascript/closure",
        "//java/google/registry/ui/js",
        "//javascript/closure:testing",
    ],
)
