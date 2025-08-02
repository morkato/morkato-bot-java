# project('mcisid', 'c', version: '1.0.0')

common_sources = files(
  'src/common/mcisid.c',
  'src/common/mcisidv1.c'
)

linux_sources = files(
  'src/linux/mcisidv1.c'
)

inc = include_directories('include')
libmcisid = library(
  'mcisid',
  common_sources + linux_sources ,
  include_directories: inc,
  install: true,
  install_dir: get_option('libdir')
)

libmcisid_dep = declare_dependency(
  link_with: libmcisid,
  include_directories: inc
)

install_headers(
  'include/mcisid/mcisid.h',
  'include/mcisid/mcisidv1.h',
  subdir: 'mcisid'
)