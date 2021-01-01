import {Component, Inject, Vue} from 'vue-property-decorator';

import {IPortfolioEntry} from '@/shared/model/portfolio-entry.model';
import PortfolioEntryService from './portfolio-entry.service';

@Component
export default class PortfolioEntryDetails extends Vue {
  public portfolioEntry: IPortfolioEntry = {};
  @Inject('portfolioEntryService') private portfolioEntryService: () => PortfolioEntryService;

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.portfolioEntryId) {
        vm.retrievePortfolioEntry(to.params.portfolioEntryId);
      }
    });
  }

  public retrievePortfolioEntry(portfolioEntryId) {
    this.portfolioEntryService().find(portfolioEntryId).then((res) => {
      this.portfolioEntry = res;
    });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
