import {IPortfolio} from "@/shared/model/portfolio.model";
import {IPortfoliosNumbers} from "@/shared/dtos/portfolio-numbers.model";

export interface IPortfolioDTO {
  portfolio?: IPortfolio;
  portfolioNumbers?: IPortfoliosNumbers;
}

export class PortfolioDTO implements IPortfolioDTO {
  constructor(
    public portfolio?: IPortfolio,
    public portfolioNumbers?: IPortfoliosNumbers
  ) {
  }
}

