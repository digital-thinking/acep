import {IPortfolio} from '@/shared/model/portfolio.model';
import {IAsset} from '@/shared/model/asset.model';

export interface IPortfolioEntry {
  id?: number;
  amount?: number;
  price?: number;
  bought?: Date;
  sold?: Date;
  customName?: string;
  group1?: string;
  group2?: string;
  group3?: string;
  group4?: string;
  portfolio?: IPortfolio;
  asset?: IAsset;
}

export class PortfolioEntry implements IPortfolioEntry {
  constructor(
    public id?: number,
    public amount?: number,
    public price?: number,
    public bought?: Date,
    public sold?: Date,
    public customName?: string,
    public group1?: string,
    public group2?: string,
    public group3?: string,
    public group4?: string,
    public portfolio?: IPortfolio,
    public asset?: IAsset,
  ) {
  }
}
