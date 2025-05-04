package org.morkato.bmt.registration.management;

import org.morkato.bmt.components.SlashMapper;
import org.morkato.bmt.generated.registries.SlashMapperRegistry;
import org.morkato.bmt.registration.MapObjectRegisterInfo;
import org.morkato.bmt.registration.MapRegistryManagement;
import org.morkato.bmt.registration.RegisterManagement;

public class MapInteractionOptionsRegistryManagement
  extends MapObjectRegisterInfo<SlashMapperRegistry<?>>
  implements RegisterManagement<SlashMapper<?>,SlashMapperRegistry<?>>,
             MapRegistryManagement<Class<?>,SlashMapperRegistry<?>> {
  @Override
  public void register(SlashMapper<?> mapper) {
    Class<?> clazz = SlashMapper.getArgument(mapper.getClass());
    this.add(clazz, new SlashMapperRegistry<>(mapper));
  }

  @Override
  public SlashMapperRegistry<?> get(Class<?> key) {
    return this.getRegistry(key);
  }
}
