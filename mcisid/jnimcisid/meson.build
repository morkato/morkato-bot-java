project('jnimcisid', 'c', version: '1.0.0')

cc = meson.get_compiler('c')
java_home = run_command('sh', '-c', 'echo $JAVA_HOME').stdout().strip()
inc = include_directories(
  '../include',
  'include',
  join_paths(java_home, 'include'),
  join_paths(java_home, 'include/linux')
)
mcisid_build_abs_path = join_paths(meson.current_source_dir(), '../builddir')
mcisid = cc.find_library('mcisid', dirs: [mcisid_build_abs_path], required: true)

lib = shared_module(
  'jnimcisid',
  sources: ['src/McisidNative.c', 'src/Mcisidv1Native.c'],
  include_directories: inc,
  dependencies: [mcisid],
  install: true,
  install_dir: get_option('libdir')
)