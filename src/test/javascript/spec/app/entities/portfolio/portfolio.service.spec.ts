/* tslint:disable max-line-length */
import axios from 'axios';
import sinon from 'sinon'
import dayjs from 'dayjs';

import {DATE_TIME_FORMAT} from '@/shared/date/filters';
import PortfolioService from '@/entities/portfolio/portfolio.service';
import {Portfolio} from '@/shared/model/portfolio.model';

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
  describe('Portfolio Service', () => {
    let service: PortfolioService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new PortfolioService();
      currentDate = new Date();
      elemDefault = new Portfolio(
        0,
        'AAAAAAA',
        currentDate,
      );
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = Object.assign({
          created: dayjs(currentDate).format(DATE_TIME_FORMAT),
        }, elemDefault);
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

      it('should create a Portfolio', async () => {
        const returnedFromService = Object.assign({
          id: 0,
          created: dayjs(currentDate).format(DATE_TIME_FORMAT),
        }, elemDefault);
        const expected = Object.assign({
          created: currentDate,
        }, returnedFromService);

        axiosStub.post.resolves({data: returnedFromService});
        return service.create({}).then((res) => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a Portfolio', async () => {
        axiosStub.post.rejects(error);

        return service.create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a Portfolio', async () => {
        const returnedFromService = Object.assign({
          name: 'BBBBBB',
          created: dayjs(currentDate).format(DATE_TIME_FORMAT),
        }, elemDefault);

        const expected = Object.assign({
          created: currentDate,
        }, returnedFromService);
        axiosStub.put.resolves({data: returnedFromService});

        return service.update(expected).then((res) => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a Portfolio', async () => {
        axiosStub.put.rejects(error);

        return service.update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of Portfolio', async () => {
        const returnedFromService = Object.assign({
          name: 'BBBBBB',
          created: dayjs(currentDate).format(DATE_TIME_FORMAT),
        }, elemDefault);
        const expected = Object.assign({
          created: currentDate,
        }, returnedFromService);
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve().then((res) => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of Portfolio', async () => {
        axiosStub.get.rejects(error);

        return service.retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a Portfolio', async () => {
        axiosStub.delete.resolves({ok: true});
        return service.delete(123).then((res) => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a Portfolio', async () => {
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
