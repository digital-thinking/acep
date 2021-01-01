/* tslint:disable max-line-length */
import {createLocalVue, shallowMount, Wrapper} from '@vue/test-utils';
import sinon, {SinonStubbedInstance} from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import PortfolioDetailComponent from '@/entities/portfolio/portfolio-details.vue';
import PortfolioClass from '@/entities/portfolio/portfolio-details.component';
import PortfolioService from '@/entities/portfolio/portfolio.service';
import router from '@/router';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {

  describe('Portfolio Management Detail Component', () => {
    let wrapper: Wrapper<PortfolioClass>;
    let comp: PortfolioClass;
    let portfolioServiceStub: SinonStubbedInstance<PortfolioService>;

    beforeEach(() => {
      portfolioServiceStub = sinon.createStubInstance<PortfolioService>(PortfolioService);

      wrapper = shallowMount<PortfolioClass>(PortfolioDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {portfolioService: () => portfolioServiceStub}
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundPortfolio = {id: 123};
        portfolioServiceStub.find.resolves(foundPortfolio);

        // WHEN
        comp.retrievePortfolio(123);
        await comp.$nextTick();

        // THEN
        expect(comp.portfolio).toBe(foundPortfolio);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundPortfolio = {id: 123};
        portfolioServiceStub.find.resolves(foundPortfolio);

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
