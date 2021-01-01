/* tslint:disable max-line-length */
import {createLocalVue, shallowMount, Wrapper} from '@vue/test-utils';
import sinon, {SinonStubbedInstance} from 'sinon';

import * as config from '@/shared/config/config';
import AssetComponent from '@/entities/asset/asset.vue';
import AssetClass from '@/entities/asset/asset.component';
import AssetService from '@/entities/asset/asset.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('b-badge', {});
localVue.directive('b-modal', {});
localVue.component('b-button', {});
localVue.component('router-link', {});

const bModalStub = {
  render: () => {
  },
  methods: {
    hide: () => {
    },
    show: () => {
    }
  }
};

describe('Component Tests', () => {
  describe('Asset Management Component', () => {
    let wrapper: Wrapper<AssetClass>;
    let comp: AssetClass;
    let assetServiceStub: SinonStubbedInstance<AssetService>;

    beforeEach(() => {
      assetServiceStub = sinon.createStubInstance<AssetService>(AssetService);
      assetServiceStub.retrieve.resolves({headers: {}});

      wrapper = shallowMount<AssetClass>(AssetComponent, {
        store,
        i18n,
        localVue,
        stubs: {bModal: bModalStub as any},
        provide: {
          assetService: () => assetServiceStub
        }
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      assetServiceStub.retrieve.resolves({headers: {}, data: [{id: 123}]});

      // WHEN
      comp.retrieveAllAssets();
      await comp.$nextTick();

      // THEN
      expect(assetServiceStub.retrieve.called).toBeTruthy();
      expect(comp.assets[0]).toEqual(jasmine.objectContaining({id: 123}));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      assetServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({id: 123});
      comp.removeAsset();
      await comp.$nextTick();

      // THEN
      expect(assetServiceStub.delete.called).toBeTruthy();
      expect(assetServiceStub.retrieve.callCount).toEqual(1);
    });
  });
});
