import {createLocalVue, shallowMount, Wrapper} from '@vue/test-utils';
import axios from 'axios';
import sinon from 'sinon';
import Logs from '@/admin/logs/logs.vue';
import LogsClass from '@/admin/logs/logs.component';
import LogsService from '@/admin/logs/logs.service';

import * as config from '@/shared/config/config';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);

const axiosStub = {
  get: sinon.stub(axios, 'get'),
  post: sinon.stub(axios, 'post'),
};

describe('Logs Component', () => {
  let wrapper: Wrapper<LogsClass>;
  let logs: LogsClass;

  beforeEach(() => {
    axiosStub.get.resolves({});
    wrapper = shallowMount<LogsClass>(Logs, {store, i18n, localVue, provide: {logsService: () => new LogsService()}});
    logs = wrapper.vm;
  });

  describe('OnInit', () => {
    it('should set all default values correctly', () => {
      expect(logs.filtered).toBe('');
      expect(logs.orderProp).toBe('name');
      expect(logs.reverse).toBe(false);
    });

    it('Should call load all on init', async () => {
      // WHEN
      logs.init();
      await logs.$nextTick();

      // THEN
      expect(axiosStub.get.calledWith('management/loggers')).toBeTruthy();
    });
  });

  describe('change log level', () => {
    it('should change log level correctly', async () => {
      axiosStub.post.resolves({});

      // WHEN
      logs.updateLevel('main', 'ERROR');
      await logs.$nextTick();

      // THEN
      expect(axiosStub.post.calledWith('management/loggers/main', {configuredLevel: 'ERROR'})).toBeTruthy();
      expect(axiosStub.get.calledWith('management/loggers')).toBeTruthy();
    });
  });

  describe('change order', () => {
    it('should change order and invert reverse', () => {
      // WHEN
      logs.changeOrder('dummy-order');

      // THEN
      expect(logs.orderProp).toEqual('dummy-order');
      expect(logs.reverse).toBe(true);
    });
  });
});
