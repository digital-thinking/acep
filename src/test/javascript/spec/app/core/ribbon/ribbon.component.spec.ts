import {createLocalVue, shallowMount, Wrapper} from '@vue/test-utils';
import Ribbon from '@/core/ribbon/ribbon.vue';
import RibbonClass from '@/core/ribbon/ribbon.component';

import * as config from '@/shared/config/config';

const localVue = createLocalVue();
config.initVueApp(localVue);
const store = config.initVueXStore(localVue);

const i18n = config.initI18N(localVue);

describe('Ribbon', () => {
  let ribbon: RibbonClass;
  let wrapper: Wrapper<RibbonClass>;

  const wrap = async (managementInfo?: any) => {
    wrapper = shallowMount<RibbonClass>(Ribbon, {
      i18n,
      store,
      localVue,
    });
    ribbon = wrapper.vm;
    await ribbon.$nextTick();
  };

  beforeEach(() => {
    store.commit('setRibbonOnProfiles', null);
  });

  it('should not have ribbonEnabled when no data', async () => {
    await wrap();
    expect(ribbon.ribbonEnabled).toBeFalsy();
  });

  it('should have ribbonEnabled set to value in store', async () => {
    const profile = 'dev';
    store.commit('setActiveProfiles', ['foo', profile, 'bar']);
    store.commit('setRibbonOnProfiles', profile);
    expect(ribbon.ribbonEnabled).toBeTruthy();
  });

  it('should not have ribbonEnabled when profile not activated', async () => {
    const profile = 'dev';
    store.commit('setActiveProfiles', ['foo', 'bar']);
    store.commit('setRibbonOnProfiles', profile);
    expect(ribbon.ribbonEnabled).toBeFalsy();
  });
});
