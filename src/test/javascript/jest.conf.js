module.exports = {
  coverageDirectory: '<rootDir>/build/test-results/',
  coveragePathIgnorePatterns: [
    '<rootDir>/node_modules/',
    '<rootDir>/src/test/javascript/',
    '<rootDir>/src/main/webapp/app/router',
    '.*.json',
  ],
  moduleFileExtensions: ['js', 'json', 'ts', 'vue'],
  transform: {
    '.*\\.(vue)$': 'vue-jest',
    '^.+\\.tsx?$': 'ts-jest',
  },
  moduleNameMapper: {
    '^@/(.*)$': '<rootDir>/src/main/webapp/app/$1',
  },
  reporters: ['default', ['jest-junit', {
    outputDirectory: './build/test-results/',
    outputName: 'TESTS-results-jest.xml'
  }]],
  testResultsProcessor: 'jest-sonar-reporter',
  testMatch: ['<rootDir>/src/test/javascript/spec/**/@(*.)@(spec.ts)'],
  snapshotSerializers: ['<rootDir>/node_modules/jest-serializer-vue'],
  rootDir: '../../../',
  coverageThreshold: {
    global: {
      statements: 80,
      branches: 60,
      functions: 70,
      lines: 80,
    },
  },
};
