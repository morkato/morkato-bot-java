project('mcisid', 'c', version: '1.0.0')

inc = include_directories('include')
libmcisid = library(
  'mcisid',
  ['src/mcisid.c', 'src/mcisidv1.c'],
  include_directories: inc,
  install: true,
  install_dir: get_option('libdir')
)

install_headers(
  'include/mcisid.h',
  'include/mcisidv1.h',
  subdir: 'mcisid'
)