/* tslint:disable max-line-length */
import {createLocalVue, shallowMount, Wrapper} from '@vue/test-utils';
import sinon, {SinonStubbedInstance} from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import AssetDetailComponent from '@/entities/asset/asset-details.vue';
import AssetClass from '@/entities/asset/asset-details.component';
import AssetService from '@/entities/asset/asset.service';
import router from '@/router';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {

  describe('Asset Management Detail Component', () => {
    let wrapper: Wrapper<AssetClass>;
    let comp: AssetClass;
    let assetServiceStub: SinonStubbedInstance<AssetService>;

    beforeEach(() => {
      assetServiceStub = sinon.createStubInstance<AssetService>(AssetService);

      wrapper = shallowMount<AssetClass>(AssetDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {assetService: () => assetServiceStub}
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundAsset = {id: 123};
        assetServiceStub.find.resolves(foundAsset);

        // WHEN
        comp.retrieveAsset(123);
        await comp.$nextTick();

        // THEN
        expect(comp.asset).toBe(foundAsset);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundAsset = {id: 123};
        assetServiceStub.find.resolves(foundAsset);

        // WHEN
        comp.beforeRouteEnter({params: {assetId: 123}}, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.asset).toBe(foundAsset);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        comp.previousState();
        await comp.$nextTick();

        expect(comp.$router.currentRoute.fullPath).toContain('/');
      });
    });
  });
});
