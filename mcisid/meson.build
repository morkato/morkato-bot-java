project('mcisid', 'C',
  version : '1.0.0',
  default_options : ['warning_level=3', 'c_std=c11'])

pg_config = find_program('pg_config', required: true)
pg_includedir = run_command(pg_config, '--includedir-server').stdout().strip()
pg_libdir = run_command(pg_config, '--pkglibdir').stdout().strip()
pg_extension_dir = join_paths(run_command(pg_config, '--sharedir').stdout().strip(), 'extension')

inc = include_directories('include', pg_includedir)

lib = shared_module('pgmcisid',
  sources: [
    'src/mcisid/mcisidv1.c',
    'src/mcisid/mcisid.c',
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