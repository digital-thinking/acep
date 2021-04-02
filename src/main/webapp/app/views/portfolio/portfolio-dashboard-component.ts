import {Component, Inject, Vue} from 'vue-property-decorator';
import PortfolioService from "@/entities/portfolio/portfolio.service";
import PortfolioView from '@/views/portfolio/portfolio-view.vue'
import {IPortfolioDTO} from "@/shared/dtos/portfolios-dto.model";

@Component({
  components: {
    "PortfolioView": PortfolioView
  }
})
export default class PortfolioDashboardComponent extends Vue {
  public portfoliosDTOs: IPortfolioDTO[] = [];
  @Inject('portfolioService') private portfolioService: () => PortfolioService;

  public mounted(): void {
    this.getAll();
  }

  public getAll(): void {
    this.portfolioService().retrieveDtos().then(res => {
      this.portfoliosDTOs = res;
    }, err => {
    });
  }


}



