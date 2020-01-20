// IAdditionService.aidl
package pl.futuredev.foregroundservice;

// Declare any non-default types here with import statements

interface IAdditionService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
  int add(in int value1, in int value2);
}
