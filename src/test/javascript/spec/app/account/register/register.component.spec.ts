import {createLocalVue, shallowMount, Wrapper} from '@vue/test-utils';
import axios from 'axios';
import sinon from 'sinon';

import {EMAIL_ALREADY_USED_TYPE, LOGIN_ALREADY_USED_TYPE} from '@/constants';
import * as config from '@/shared/config/config';
import Register from '@/account/register/register.vue';
import RegisterClass from '@/account/register/register.component';
import RegisterService from '@/account/register/register.service';
import LoginService from '@/account/login.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);

const axiosStub = {
  get: sinon.stub(axios, 'get'),
  post: sinon.stub(axios, 'post'),
};

describe('Register Component', () => {
  let wrapper: Wrapper<RegisterClass>;
  let register: RegisterClass;
  const filledRegisterAccount = {email: 'jhi@pster.net', langKey: 'de', login: 'jhi', password: 'jhipster'};

  beforeEach(() => {
    axiosStub.get.resolves({});
    axiosStub.post.reset();

    wrapper = shallowMount<RegisterClass>(Register, {
      store,
      i18n,
      localVue,
      provide: {
        registerService: () => new RegisterService(),
        loginService: () => new LoginService(),
      },
    });
    register = wrapper.vm;
  });

  it('should set all default values correctly', () => {
    expect(register.success).toBe(false);
    expect(register.error).toBe('');
    expect(register.errorEmailExists).toBe('');
    expect(register.errorUserExists).toBe('');
    expect(register.confirmPassword).toBe(null);
    expect(register.registerAccount.login).toBe(undefined);
    expect(register.registerAccount.password).toBe(undefined);
    expect(register.registerAccount.email).toBe(undefined);
  });

  it('should open login modal when asked to', () => {
    let called = false;
    register.$root.$on('bv::show::modal', () => {
      called = true;
    });
    register.openLogin();
    expect(called).toBe(true);
  });

  it('should register when password match', async () => {
    axiosStub.post.resolves();
    register.registerAccount = filledRegisterAccount;
    register.confirmPassword = filledRegisterAccount.password;
    register.register();
    await register.$nextTick();

    expect(
      axiosStub.post.calledWith('api/register', {
        email: 'jhi@pster.net',
        langKey: 'de',
        login: 'jhi',
        password: 'jhipster'
      })
    ).toBeTruthy();
    expect(register.success).toBe(true);
    expect(register.error).toBe(null);
    expect(register.errorEmailExists).toBe(null);
    expect(register.errorUserExists).toBe(null);
  });

  it('should register when password match but throw error when login already exist', async () => {
    const error = {response: {status: 400, data: {type: LOGIN_ALREADY_USED_TYPE}}};
    axiosStub.post.rejects(error);
    register.registerAccount = filledRegisterAccount;
    register.confirmPassword = filledRegisterAccount.password;
    register.register();
    await register.$nextTick();

    expect(
      axiosStub.post.calledWith('api/register', {
        email: 'jhi@pster.net',
        langKey: 'de',
        login: 'jhi',
        password: 'jhipster'
      })
    ).toBeTruthy();
    await register.$nextTick();
    expect(register.success).toBe(null);
    expect(register.error).toBe(null);
    expect(register.errorEmailExists).toBe(null);
    expect(register.errorUserExists).toBe('ERROR');
  });

  it('should register when password match but throw error when email already used', async () => {
    const error = {response: {status: 400, data: {type: EMAIL_ALREADY_USED_TYPE}}};
    axiosStub.post.rejects(error);
    register.registerAccount = filledRegisterAccount;
    register.confirmPassword = filledRegisterAccount.password;
    register.register();
    await register.$nextTick();

    expect(
      axiosStub.post.calledWith('api/register', {
        email: 'jhi@pster.net',
        langKey: 'de',
        login: 'jhi',
        password: 'jhipster'
      })
    ).toBeTruthy();
    await register.$nextTick();
    expect(register.success).toBe(null);
    expect(register.error).toBe(null);
    expect(register.errorEmailExists).toBe('ERROR');
    expect(register.errorUserExists).toBe(null);
  });

  it('should register when password match but throw error', async () => {
    const error = {response: {status: 400, data: {type: 'unknown'}}};
    axiosStub.post.rejects(error);
    register.registerAccount = filledRegisterAccount;
    register.confirmPassword = filledRegisterAccount.password;
    register.register();
    await register.$nextTick();

    expect(
      axiosStub.post.calledWith('api/register', {
        email: 'jhi@pster.net',
        langKey: 'de',
        login: 'jhi',
        password: 'jhipster'
      })
    ).toBeTruthy();
    await register.$nextTick();
    expect(register.success).toBe(null);
    expect(register.errorEmailExists).toBe(null);
    expect(register.errorUserExists).toBe(null);
    expect(register.error).toBe('ERROR');
  });
});
