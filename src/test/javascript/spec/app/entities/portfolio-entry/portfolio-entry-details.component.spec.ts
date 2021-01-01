/* tslint:disable max-line-length */
import {createLocalVue, shallowMount, Wrapper} from '@vue/test-utils';
import sinon, {SinonStubbedInstance} from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import PortfolioEntryDetailComponent from '@/entities/portfolio-entry/portfolio-entry-details.vue';
import PortfolioEntryClass from '@/entities/portfolio-entry/portfolio-entry-details.component';
import PortfolioEntryService from '@/entities/portfolio-entry/portfolio-entry.service';
import router from '@/router';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {

  describe('PortfolioEntry Management Detail Component', () => {
    let wrapper: Wrapper<PortfolioEntryClass>;
    let comp: PortfolioEntryClass;
    let portfolioEntryServiceStub: SinonStubbedInstance<PortfolioEntryService>;

    beforeEach(() => {
      portfolioEntryServiceStub = sinon.createStubInstance<PortfolioEntryService>(PortfolioEntryService);

      wrapper = shallowMount<PortfolioEntryClass>(PortfolioEntryDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {portfolioEntryService: () => portfolioEntryServiceStub}
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundPortfolioEntry = {id: 123};
        portfolioEntryServiceStub.find.resolves(foundPortfolioEntry);

        // WHEN
        comp.retrievePortfolioEntry(123);
        await comp.$nextTick();

        // THEN
        expect(comp.portfolioEntry).toBe(foundPortfolioEntry);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundPortfolioEntry = {id: 123};
        portfolioEntryServiceStub.find.resolves(foundPortfolioEntry);

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
