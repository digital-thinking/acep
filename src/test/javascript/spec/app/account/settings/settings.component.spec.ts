import {createLocalVue, shallowMount, Wrapper} from '@vue/test-utils';
import axios from 'axios';
import sinon from 'sinon';

import * as config from '@/shared/config/config';
import Settings from '@/account/settings/settings.vue';
import SettingsClass from '@/account/settings/settings.component';
import {EMAIL_ALREADY_USED_TYPE} from '@/constants';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);

const axiosStub = {
  get: sinon.stub(axios, 'get'),
  post: sinon.stub(axios, 'post'),
};

describe('Settings Component', () => {
  let wrapper: Wrapper<SettingsClass>;
  let settings: SettingsClass;
  const account = {
    firstName: 'John',
    lastName: 'Doe',
    email: 'john.doe@jhipster.org',
  };

  beforeEach(() => {
    axiosStub.get.resolves({});
    axiosStub.post.reset();

    store.commit('authenticated', account);
    wrapper = shallowMount<SettingsClass>(Settings, {
      store,
      i18n,
      localVue,
    });
    settings = wrapper.vm;
  });

  it('should send the current identity upon save', async () => {
    // GIVEN
    axiosStub.post.resolves({});

    // WHEN
    settings.save();
    await settings.$nextTick();

    // THEN
    expect(axiosStub.post.calledWith('api/account', account)).toBeTruthy();
  });

  it('should notify of success upon successful save', async () => {
    // GIVEN
    axiosStub.post.resolves(account);

    // WHEN
    settings.save();
    await settings.$nextTick();

    // THEN
    expect(settings.error).toBeNull();
    expect(settings.success).toBe('OK');
  });

  it('should notify of error upon failed save', async () => {
    // GIVEN
    const error = {response: {status: 417}};
    axiosStub.post.rejects(error);

    // WHEN
    settings.save();
    await settings.$nextTick();

    // THEN
    expect(settings.error).toEqual('ERROR');
    expect(settings.errorEmailExists).toBeNull();
    expect(settings.success).toBeNull();
  });

  it('should notify of error upon error 400', async () => {
    // GIVEN
    const error = {response: {status: 400, data: {}}};
    axiosStub.post.rejects(error);

    // WHEN
    settings.save();
    await settings.$nextTick();

    // THEN
    expect(settings.error).toEqual('ERROR');
    expect(settings.errorEmailExists).toBeNull();
    expect(settings.success).toBeNull();
  });

  it('should notify of error upon email already used', async () => {
    // GIVEN
    const error = {response: {status: 400, data: {type: EMAIL_ALREADY_USED_TYPE}}};
    axiosStub.post.rejects(error);

    // WHEN
    settings.save();
    await settings.$nextTick();

    // THEN
    expect(settings.errorEmailExists).toEqual('ERROR');
    expect(settings.error).toBeNull();
    expect(settings.success).toBeNull();
  });
});
