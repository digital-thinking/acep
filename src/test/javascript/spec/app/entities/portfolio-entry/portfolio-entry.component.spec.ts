/* tslint:disable max-line-length */
import {createLocalVue, shallowMount, Wrapper} from '@vue/test-utils';
import sinon, {SinonStubbedInstance} from 'sinon';

import * as config from '@/shared/config/config';
import PortfolioEntryComponent from '@/entities/portfolio-entry/portfolio-entry.vue';
import PortfolioEntryClass from '@/entities/portfolio-entry/portfolio-entry.component';
import PortfolioEntryService from '@/entities/portfolio-entry/portfolio-entry.service';

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
  describe('PortfolioEntry Management Component', () => {
    let wrapper: Wrapper<PortfolioEntryClass>;
    let comp: PortfolioEntryClass;
    let portfolioEntryServiceStub: SinonStubbedInstance<PortfolioEntryService>;

    beforeEach(() => {
      portfolioEntryServiceStub = sinon.createStubInstance<PortfolioEntryService>(PortfolioEntryService);
      portfolioEntryServiceStub.retrieve.resolves({headers: {}});

      wrapper = shallowMount<PortfolioEntryClass>(PortfolioEntryComponent, {
        store,
        i18n,
        localVue,
        stubs: {bModal: bModalStub as any},
        provide: {
          portfolioEntryService: () => portfolioEntryServiceStub
        }
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      portfolioEntryServiceStub.retrieve.resolves({headers: {}, data: [{id: 123}]});

      // WHEN
      comp.retrieveAllPortfolioEntrys();
      await comp.$nextTick();

      // THEN
      expect(portfolioEntryServiceStub.retrieve.called).toBeTruthy();
      expect(comp.portfolioEntries[0]).toEqual(jasmine.objectContaining({id: 123}));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      portfolioEntryServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({id: 123});
      comp.removePortfolioEntry();
      await comp.$nextTick();

      // THEN
      expect(portfolioEntryServiceStub.delete.called).toBeTruthy();
      expect(portfolioEntryServiceStub.retrieve.callCount).toEqual(1);
    });
  });
});
