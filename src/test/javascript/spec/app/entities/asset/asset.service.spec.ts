/* tslint:disable max-line-length */
import axios from 'axios';
import sinon from 'sinon'

import AssetService from '@/entities/asset/asset.service';
import {Asset} from '@/shared/model/asset.model';
import {Currency} from '@/shared/model/enumerations/currency.model';
import {AssetType} from '@/shared/model/enumerations/asset-type.model';
import {Source} from '@/shared/model/enumerations/source.model';

const error = {
  response: {
    status: null,
    data: {
      type: null
    }
  }
};

const axiosStub = {
  get: sinon.stub(axios, 'get'),
  post: sinon.stub(axios, 'post'),
  put: sinon.stub(axios, 'put'),
  delete: sinon.stub(axios, 'delete'),
};

describe('Service Tests', () => {
  describe('Asset Service', () => {
    let service: AssetService;
    let elemDefault;

    beforeEach(() => {
      service = new AssetService();
      elemDefault = new Asset(
        0,
        'AAAAAAA',
        Currency.AED,
        AssetType.Stock,
        'AAAAAAA',
        Source.AlphaVantage,
      );
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = Object.assign({}, elemDefault);
        axiosStub.get.resolves({data: returnedFromService});

        return service
          .find(123).then((res) => {
            expect(res).toMatchObject(elemDefault);
          });
      });

      it('should not find an element', async () => {
        axiosStub.get.rejects(error);
        return service.find(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should create a Asset', async () => {
        const returnedFromService = Object.assign({
          id: 0,
        }, elemDefault);
        const expected = Object.assign({}, returnedFromService);

        axiosStub.post.resolves({data: returnedFromService});
        return service.create({}).then((res) => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a Asset', async () => {
        axiosStub.post.rejects(error);

        return service.create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a Asset', async () => {
        const returnedFromService = Object.assign({
          name: 'BBBBBB',
          currency: 'BBBBBB',
          assetType: 'BBBBBB',
          symbol: 'BBBBBB',
          source: 'BBBBBB',
        }, elemDefault);

        const expected = Object.assign({}, returnedFromService);
        axiosStub.put.resolves({data: returnedFromService});

        return service.update(expected).then((res) => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a Asset', async () => {
        axiosStub.put.rejects(error);

        return service.update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of Asset', async () => {
        const returnedFromService = Object.assign({
          name: 'BBBBBB',
          currency: 'BBBBBB',
          assetType: 'BBBBBB',
          symbol: 'BBBBBB',
          source: 'BBBBBB',
        }, elemDefault);
        const expected = Object.assign({}, returnedFromService);
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve().then((res) => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of Asset', async () => {
        axiosStub.get.rejects(error);

        return service.retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a Asset', async () => {
        axiosStub.delete.resolves({ok: true});
        return service.delete(123).then((res) => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a Asset', async () => {
        axiosStub.delete.rejects(error);

        return service.delete(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });
    });
  });
});
