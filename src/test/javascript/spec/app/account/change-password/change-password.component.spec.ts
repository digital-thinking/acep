import {createLocalVue, shallowMount, Wrapper} from '@vue/test-utils';
import axios from 'axios';
import sinon from 'sinon';

import * as config from '@/shared/config/config';
import ChangePassword from '@/account/change-password/change-password.vue';
import ChangePasswordClass from '@/account/change-password/change-password.component';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);

const axiosStub = {
  get: sinon.stub(axios, 'get'),
  post: sinon.stub(axios, 'post'),
};

describe('ChangePassword Component', () => {
  let wrapper: Wrapper<ChangePasswordClass>;
  let changePassword: ChangePasswordClass;

  beforeEach(() => {
    axiosStub.get.resolves({});
    axiosStub.post.reset();

    wrapper = shallowMount<ChangePasswordClass>(ChangePassword, {
      store,
      i18n,
      localVue,
    });
    changePassword = wrapper.vm;
  });

  it('should show error if passwords do not match', () => {
    // GIVEN
    changePassword.resetPassword = {newPassword: 'password1', confirmPassword: 'password2'};
    // WHEN
    changePassword.changePassword();
    // THEN
    expect(changePassword.doNotMatch).toBe('ERROR');
    expect(changePassword.error).toBeNull();
    expect(changePassword.success).toBeNull();
  });

  it('should call Auth.changePassword when passwords match and  set success to OK upon success', async () => {
    // GIVEN
    changePassword.resetPassword = {
      currentPassword: 'password1',
      newPassword: 'password1',
      confirmPassword: 'password1'
    };
    axiosStub.post.resolves({});

    // WHEN
    changePassword.changePassword();
    await changePassword.$nextTick();

    // THEN
    expect(
      axiosStub.post.calledWith('api/account/change-password', {
        currentPassword: 'password1',
        newPassword: 'password1',
      })
    ).toBeTruthy();

    expect(changePassword.doNotMatch).toBeNull();
    expect(changePassword.error).toBeNull();
    expect(changePassword.success).toBe('OK');
  });

  it('should notify of error if change password fails', async () => {
    // GIVEN
    changePassword.resetPassword = {
      currentPassword: 'password1',
      newPassword: 'password1',
      confirmPassword: 'password1'
    };
    axiosStub.post.rejects({});

    // WHEN
    changePassword.changePassword();
    await changePassword.$nextTick();

    // THEN
    expect(changePassword.doNotMatch).toBeNull();
    expect(changePassword.success).toBeNull();
    await changePassword.$nextTick();
    expect(changePassword.error).toBe('ERROR');
  });
});
