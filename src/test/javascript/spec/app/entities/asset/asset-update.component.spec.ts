/* tslint:disable max-line-length */
import {createLocalVue, shallowMount, Wrapper} from '@vue/test-utils';
import sinon, {SinonStubbedInstance} from 'sinon';
import Router from 'vue-router';


import * as config from '@/shared/config/config';
import AssetUpdateComponent from '@/entities/asset/asset-update.vue';
import AssetClass from '@/entities/asset/asset-update.component';
import AssetService from '@/entities/asset/asset.service';


import PortfolioEntryService from '@/entities/portfolio-entry/portfolio-entry.service';


const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.component('font-awesome-icon', {});
localVue.component('b-input-group', {});
localVue.component('b-input-group-prepend', {});
localVue.component('b-form-datepicker', {});
localVue.component('b-form-input', {});

describe('Component Tests', () => {

  describe('Asset Management Update Component', () => {
    let wrapper: Wrapper<AssetClass>;
    let comp: AssetClass;
    let assetServiceStub: SinonStubbedInstance<AssetService>;

    beforeEach(() => {
      assetServiceStub = sinon.createStubInstance<AssetService>(AssetService);

      wrapper = shallowMount<AssetClass>(AssetUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          assetService: () => assetServiceStub,

          portfolioEntryService: () => new PortfolioEntryService(),

        }
      });
      comp = wrapper.vm;
    });


    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = {id: 123};
        comp.asset = entity;
        assetServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(assetServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.asset = entity;
        assetServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(assetServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundAsset = {id: 123};
        assetServiceStub.find.resolves(foundAsset);
        assetServiceStub.retrieve.resolves([foundAsset]);

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
