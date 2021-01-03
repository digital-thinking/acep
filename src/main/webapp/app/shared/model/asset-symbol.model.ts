export interface IAssetSymbol {
  symbol?: string;
  name?: string;
  type?: string;
  region?: string;
  marketOpenTime?: string;
  marketCloseTime?: string;
  timezone?: string;
  currency?: string;
  score?: string;
}

export class AssetSymbol implements IAssetSymbol {
  constructor(
    public symbol?: string,
    public name?: string,
    public type?: string,
    public region?: string,
    public marketOpenTime?: string,
    public marketCloseTime?: string,
    public timezone?: string,
    public currency?: string,
    public score?: string,
  ) {
  }
}
