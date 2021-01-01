/* tslint:disable max-line-length */
import {createLocalVue, shallowMount, Wrapper} from '@vue/test-utils';
import sinon, {SinonStubbedInstance} from 'sinon';
import Router from 'vue-router';

import dayjs from 'dayjs';
import {DATE_TIME_LONG_FORMAT} from '@/shared/date/filters';

import * as config from '@/shared/config/config';
import PortfolioEntryUpdateComponent from '@/entities/portfolio-entry/portfolio-entry-update.vue';
import PortfolioEntryClass from '@/entities/portfolio-entry/portfolio-entry-update.component';
import PortfolioEntryService from '@/entities/portfolio-entry/portfolio-entry.service';


import PortfolioService from '@/entities/portfolio/portfolio.service';


import AssetService from '@/entities/asset/asset.service';


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

  describe('PortfolioEntry Management Update Component', () => {
    let wrapper: Wrapper<PortfolioEntryClass>;
    let comp: PortfolioEntryClass;
    let portfolioEntryServiceStub: SinonStubbedInstance<PortfolioEntryService>;

    beforeEach(() => {
      portfolioEntryServiceStub = sinon.createStubInstance<PortfolioEntryService>(PortfolioEntryService);

      wrapper = shallowMount<PortfolioEntryClass>(PortfolioEntryUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          portfolioEntryService: () => portfolioEntryServiceStub,

          portfolioService: () => new PortfolioService(),

          assetService: () => new AssetService(),

        }
      });
      comp = wrapper.vm;
    });

    describe('load', () => {
      it('Should convert date from string', () => {
        // GIVEN
        const date = new Date('2019-10-15T11:42:02Z');

        // WHEN
        const convertedDate = comp.convertDateTimeFromServer(date);

        // THEN
        expect(convertedDate).toEqual(dayjs(date).format(DATE_TIME_LONG_FORMAT));
      });

      it('Should not convert date if date is not present', () => {
        expect(comp.convertDateTimeFromServer(null)).toBeNull();
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = {id: 123};
        comp.portfolioEntry = entity;
        portfolioEntryServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(portfolioEntryServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.portfolioEntry = entity;
        portfolioEntryServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(portfolioEntryServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundPortfolioEntry = {id: 123};
        portfolioEntryServiceStub.find.resolves(foundPortfolioEntry);
        portfolioEntryServiceStub.retrieve.resolves([foundPortfolioEntry]);

        // WHEN
        comp.beforeRouteEnter({params: {portfolioEntryId: 123}}, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.portfolioEntry).toBe(foundPortfolioEntry);
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
