/* tslint:disable max-line-length */
import {createLocalVue, shallowMount, Wrapper} from '@vue/test-utils';
import sinon, {SinonStubbedInstance} from 'sinon';

import * as config from '@/shared/config/config';
import PortfolioComponent from '@/entities/portfolio/portfolio.vue';
import PortfolioClass from '@/entities/portfolio/portfolio.component';
import PortfolioService from '@/entities/portfolio/portfolio.service';

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
  describe('Portfolio Management Component', () => {
    let wrapper: Wrapper<PortfolioClass>;
    let comp: PortfolioClass;
    let portfolioServiceStub: SinonStubbedInstance<PortfolioService>;

    beforeEach(() => {
      portfolioServiceStub = sinon.createStubInstance<PortfolioService>(PortfolioService);
      portfolioServiceStub.retrieve.resolves({headers: {}});

      wrapper = shallowMount<PortfolioClass>(PortfolioComponent, {
        store,
        i18n,
        localVue,
        stubs: {bModal: bModalStub as any},
        provide: {
          portfolioService: () => portfolioServiceStub
        }
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      portfolioServiceStub.retrieve.resolves({headers: {}, data: [{id: 123}]});

      // WHEN
      comp.retrieveAllPortfolios();
      await comp.$nextTick();

      // THEN
      expect(portfolioServiceStub.retrieve.called).toBeTruthy();
      expect(comp.portfolios[0]).toEqual(jasmine.objectContaining({id: 123}));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      portfolioServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({id: 123});
      comp.removePortfolio();
      await comp.$nextTick();

      // THEN
      expect(portfolioServiceStub.delete.called).toBeTruthy();
      expect(portfolioServiceStub.retrieve.callCount).toEqual(1);
    });
  });
});
