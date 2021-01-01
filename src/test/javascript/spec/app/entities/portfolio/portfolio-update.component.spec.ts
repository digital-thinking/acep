/* tslint:disable max-line-length */
import {createLocalVue, shallowMount, Wrapper} from '@vue/test-utils';
import sinon, {SinonStubbedInstance} from 'sinon';
import Router from 'vue-router';

import dayjs from 'dayjs';
import {DATE_TIME_LONG_FORMAT} from '@/shared/date/filters';

import * as config from '@/shared/config/config';
import PortfolioUpdateComponent from '@/entities/portfolio/portfolio-update.vue';
import PortfolioClass from '@/entities/portfolio/portfolio-update.component';
import PortfolioService from '@/entities/portfolio/portfolio.service';


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

  describe('Portfolio Management Update Component', () => {
    let wrapper: Wrapper<PortfolioClass>;
    let comp: PortfolioClass;
    let portfolioServiceStub: SinonStubbedInstance<PortfolioService>;

    beforeEach(() => {
      portfolioServiceStub = sinon.createStubInstance<PortfolioService>(PortfolioService);

      wrapper = shallowMount<PortfolioClass>(PortfolioUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          portfolioService: () => portfolioServiceStub,

          portfolioEntryService: () => new PortfolioEntryService(),

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
        comp.portfolio = entity;
        portfolioServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(portfolioServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.portfolio = entity;
        portfolioServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(portfolioServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundPortfolio = {id: 123};
        portfolioServiceStub.find.resolves(foundPortfolio);
        portfolioServiceStub.retrieve.resolves([foundPortfolio]);

        // WHEN
        comp.beforeRouteEnter({params: {portfolioId: 123}}, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.portfolio).toBe(foundPortfolio);
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
