package com.mycompany.app.unsafe;


import sun.misc.Unsafe;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * Most methods of the unsafe class are native.
 * http://mishadoff.com/blog/java-magic-part-4-sun-dot-misc-dot-unsafe/
 */
public class UnsafeZone{
    public static Unsafe getUnsafe()  {
        Unsafe unsafe= null;
        try {
            Field f  = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            unsafe = (Unsafe) f.get(null);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return unsafe;
    }



    public static void main(String[] args) throws Exception {
        Unsafe unsafe = getUnsafe();

        avoidInitialization(unsafe);

        bypassSecurityAndCorruptMemory(unsafe);

        sumSuperArray();

        getUnsafe().throwException(new IOException());
    }

    private static void sumSuperArray() {
        long SUPER_SIZE = (long)Integer.MAX_VALUE * 2;
        long sum = 0;
        SuperArray array = new SuperArray(SUPER_SIZE);
        System.out.println("Array size:" + array.size()); // 4294967294
        for (int i = 0; i < 100; i++) {
            array.set((long)Integer.MAX_VALUE + i, (byte)3);
            sum += array.get((long)Integer.MAX_VALUE + i);
        }
        System.out.println("Sum of 100 elements:" + sum);  // 3
    }

    private static void bypassSecurityAndCorruptMemory(Unsafe unsafe) throws NoSuchFieldException {
        Guard guard = new Guard();
        System.out.println(guard.giveAccess());   // false, no access

        // bypass

        Field f = guard.getClass().getDeclaredField("ACCESS_ALLOWED");
        unsafe.putInt(guard, unsafe.objectFieldOffset(f), 42); // memory corruption

        System.out.println(guard.giveAccess()); // true, access granted
    }

    private static void avoidInitialization(Unsafe unsafe) throws InstantiationException, IllegalAccessException {
        A o1 = new A(); // constructor
        System.out.println(o1.a()); // prints 1

        A o2 = A.class.newInstance(); // reflection
        System.out.println(o2.a()); // prints 1

        A o3 = (A) unsafe.allocateInstance(A.class); // unsafe
        System.out.println(o3.a()); // prints 0
    }


}

/**
 * Avoid initialization and create extra singletons
 */
class A {
    private long a; // not initialized value

    public A() {
        this.a = 1; // initialization
    }

    public long a() { return this.a; }
}

/*
Memory corruption
 */
class Guard {
    private int ACCESS_ALLOWED = 1;

    public boolean giveAccess() {
        return 42 == ACCESS_ALLOWED;
    }
}

/**
 * java nio employs the direct byte buffer.
 */
class SuperArray {
    private final static int BYTE = 1;

    private long size;
    private long address;

    public SuperArray(long size) {
        this.size = size;
        address = UnsafeZone.getUnsafe().allocateMemory(size * BYTE);
    }

    public void set(long i, byte value) {
        UnsafeZone.getUnsafe().putByte(address + i * BYTE, value);
    }

    public int get(long idx) {
        return UnsafeZone.getUnsafe().getByte(address + idx * BYTE);
    }

    public long size() {
        return size;
    }
}

interface Counter {
    void increment();
    long getCounter();
}

/**
 * compare and swap to avoid lock.
 * Similarly, atomic classes also employs compare-and-swap operations.
 */
class CASCounter implements Counter {
    private volatile long counter = 0;
    private Unsafe unsafe;
    private long offset;

    public CASCounter() throws Exception {
        unsafe = Unsafe.getUnsafe();
        offset = unsafe.objectFieldOffset(CASCounter.class.getDeclaredField("counter"));
    }

    @Override
    public void increment() {
        long before = counter;
        while (!unsafe.compareAndSwapLong(this, offset, before, before + 1)) {
            before = counter;
        }
    }

    @Override
    public long getCounter() {
        return counter;
    }

}