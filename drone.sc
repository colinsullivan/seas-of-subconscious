Quarks.gui;

({
  var m, mBounds;
 
  s.quit;
  
  /*s.options.inDevice = "PreSonus FIREPOD (2112)";*/
  /*s.options.inDevice = "SF + 1818";*/
  /*s.options.inDevice = "AudioBox 1818 VSL ";*/
  /*s.options.inDevice = "Soundflower (64ch)";*/

  s.options.outDevice = "Soundflower (64ch)";
  s.options.sampleRate = 48000;
  s.options.blockSize = 8;
  s.boot();
  m = s.meter();

  // move level meter to bottom right of screen
  mBounds = m.window.bounds;
  /*mBounds.left = 1680;
  mBounds.top = 1000;*/
  mBounds.left = 1440;
  mBounds.top = 900;
  
  m.window.setTopLeftBounds(mBounds);

  Instr.dir = "~/Projects/seas-of-subconscious/lib/";
  Instr.loadAll();
  /*Library.postTree;*/
  
  /*s.doWhenBooted({
  
  });*/


}.value();)

({

  var clock;

  clock = TempoClock.new(140 / 60);

  /*~test = Pbind(
    \type, \instr,
    \instr, "test",
    \degree, Pseq([0, 2, \rest, 6, \rest]),
    \octave, 3,
    \sustain, 7,
    \dur, 8,
    \amp, 0.5
  ).play(clock);*/

  {
    ~test = Patch("SeasOfSubconsciousDrone", (
      freq: 110,
      gate: KrNumberEditor.new(0, \gate.asSpec()),
      attackTime: 2.0,
      releaseTime: 2.0
    )).play();

    1.0.wait();

    ~test.set(\gate, 1);

    2.0.wait();

    4.0.wait();

    ~test.set(\gate, 0);

    2.0.wait();

    ~test.stop();

    ~test = nil;
  
  }.fork();

  {
    ~test2 = Patch("cs.sfx.LoopBuf", (
      
    ))
  }

  
}.value();)

({


}.value();)
