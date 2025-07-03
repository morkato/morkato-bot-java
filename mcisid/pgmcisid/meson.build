project('pgmcisid', 'C',
  version : '1.0.0',
  default_options : ['warning_level=3', 'c_std=c11'])

cc = meson.get_compiler('c')
pg_config = find_program('pg_config', required: true)
pg_includedir = run_command(pg_config, '--includedir-server').stdout().strip()
pg_libdir = run_command(pg_config, '--pkglibdir').stdout().strip()
pg_extension_dir = join_paths(run_command(pg_config, '--sharedir').stdout().strip(), 'extension')

mcisid_build_abs_path = join_paths(meson.current_source_dir(), '../builddir')
inc = include_directories('include', '../include', pg_includedir)
mcisidlib = cc.find_library('mcisid', dirs: [mcisid_build_abs_path], required: true)
lib = shared_module('pgmcisid',
  sources: [
    'src/pgmcisid.c'
  ],
  include_directories: inc,
  install: true,
  install_dir: pg_libdir,
  dependencies: [],
  c_args: ['-fPIC']
)

install_data('pgmcisid.control', 
             'pgmcisid--1.0.sql',
             install_dir: pg_extension_dir)