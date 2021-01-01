import {Component, Inject, Vue} from 'vue-property-decorator';

import {IPortfolio} from '@/shared/model/portfolio.model';
import PortfolioService from './portfolio.service';

@Component
export default class PortfolioDetails extends Vue {
  public portfolio: IPortfolio = {};
  @Inject('portfolioService') private portfolioService: () => PortfolioService;

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.portfolioId) {
        vm.retrievePortfolio(to.params.portfolioId);
      }
    });
  }

  public retrievePortfolio(portfolioId) {
    this.portfolioService().find(portfolioId).then((res) => {
      this.portfolio = res;
    });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
