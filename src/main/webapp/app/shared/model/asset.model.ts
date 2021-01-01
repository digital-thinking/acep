import {IPortfolioEntry} from '@/shared/model/portfolio-entry.model';

import {Currency} from '@/shared/model/enumerations/currency.model';
import {AssetType} from '@/shared/model/enumerations/asset-type.model';
import {Source} from '@/shared/model/enumerations/source.model';

export interface IAsset {
  id?: number;
  name?: string;
  currency?: Currency;
  assetType?: AssetType;
  symbol?: string;
  source?: Source;
  portfolioEntries?: IPortfolioEntry[];
}

export class Asset implements IAsset {
  constructor(
    public id?: number,
    public name?: string,
    public currency?: Currency,
    public assetType?: AssetType,
    public symbol?: string,
    public source?: Source,
    public portfolioEntries?: IPortfolioEntry[],
  ) {
  }
}
