import {IPortfolioEntry} from '@/shared/model/portfolio-entry.model';

export interface IPortfolio {
  id?: number;
  name?: string;
  created?: Date;
  portfolioEntries?: IPortfolioEntry[];
}

export class Portfolio implements IPortfolio {
  constructor(
    public id?: number,
    public name?: string,
    public created?: Date,
    public portfolioEntries?: IPortfolioEntry[],
  ) {
  }
}
