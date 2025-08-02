# project('monarch', 'c', version: '1.0.0')

inc = include_directories('../mcisid/include', 'include')
openssl_dep = dependency('openssl')
common_libsources = files(
  'src/common/monarch.c',
  'src/common/entity_context.c',
  'src/common/uid.c',
  'src/common/log.c'
)
linux_libsources = files(
  'src/linux/monarch.c',
  'src/linux/arts.c',
  'src/linux/store.c',
  'src/linux/uid.c',
  'src/linux/log.c',
  'src/linux/util.c'
)
libmonarch = shared_library(
  'monarch',
  common_libsources + linux_libsources,
  include_directories: inc,
  dependencies: [libmcisid_dep, openssl_dep],
  install: true,
  install_dir: get_option('libdir')
)

libmonarch_dep = declare_dependency(
  link_with: libmonarch,
  include_directories: inc
)

install_headers(
  'include/monarch/monarch.h',
  'include/monarch/fields.h',
  'include/monarch/arts.h',
  'include/monarch/entity_context.h',
  'include/monarch/store.h',
  'include/monarch/uid.h',
  'include/monarch/log.h',
  subdir: 'monarch'
)

executable('simple_test_exc', 'test/simple-test.c',
  include_directories: inc,
  dependencies: [libmcisid_dep, libmonarch_dep, openssl_dep],
  install: false)