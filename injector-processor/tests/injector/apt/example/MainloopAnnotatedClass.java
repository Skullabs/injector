/*
 * Copyright 2019 Skullabs Contributors (https://github.com/skullabs)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package injector.apt.example;

import injector.Mainloop;
import injector.Singleton;

@Singleton
public class MainloopAnnotatedClass {

    @Mainloop
    void execute(){
        System.out.println("1 + 1 = 2");
    }

    @Mainloop(instances = 2, gracefulShutdownTime = 30)
    void execute2(){
        System.out.println("2 + 2 = 4");
    }
}
