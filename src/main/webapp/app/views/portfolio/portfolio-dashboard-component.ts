import {Component, Inject, Vue} from 'vue-property-decorator';
import {IPortfolio} from "@/shared/model/portfolio.model";
import PortfolioService from "@/entities/portfolio/portfolio.service";
import PortfolioView from '@/views/portfolio/portfolio-view.vue'

@Component({
  components: {
    "PortfolioView": PortfolioView
  }
})
export default class PortfolioDashboardComponent extends Vue {
  public portfolios: IPortfolio[] = [];
  @Inject('portfolioService') private portfolioService: () => PortfolioService;

  public mounted(): void {
    this.getAll();
  }

  public getAll(): void {
    this.portfolioService().retrieve().then(res => {
      this.portfolios = res.data;
    }, err => {
    });
  }


}



