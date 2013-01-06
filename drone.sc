Quarks.gui;

({
  var m,
    mBounds,
    projRoot = "/Users/colin/Projects/seas-of-subconscious",
    projSfx = projRoot +/+ "raw-samples";

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

  Instr.dir = projRoot +/+ "lib/";
  Instr.loadAll();

  s.doWhenBooted({
    var doneLoading,
      x,
      soundsOfSubConsciousSeas;

    soundsOfSubConsciousSeas = SoundsOfSubconsciousSeas.new();
    soundsOfSubConsciousSeas.init();


    /*doneLoading = {
      arg splashingWaterBuf;


      ~test = Patch("SeasOfSubconsciousDrone", (
        freq: 110,
        gate: KrNumberEditor.new(1, \gate.asSpec()),
        attackTime: 2.0,
        releaseTime: 2.0
      ));

      ~testChan = MixerChannel.new(\drone, Server.default, 2, 2, level: 0);
      ~testChan.guiUpdateTime = 0.05;

      ~testChan.play(~test);

      {

        1.0.wait();
        
        ~testChan.levelTo(1.0, 2.0);

        4.0.wait();

        ~testChan.levelTo(0.0, 2.0);

        2.0.wait();

        ~test.stop();

        ~test = nil;
      
      }.fork();

      [>{
        ~test2 = Patch("cs.sfx.LoopBuf", (
          buf: splashingWaterBuf,
          gate: KrNumberEditor.new(0, \gate.asSpec())
        )).play();

        1.0.wait();

        ~test2.set(\gate, 1);

        1.0.wait();

        10.0.wait();

        ~test2.set(\gate, 0);

        2.0.wait();

        ~test2.stop();

        ~test2 = nil;

      }.fork();
    <]
    };*/

    Buffer.read(
      Server.default,
      projSfx +/+ "58411__sinatra314__shorewaves1004.wav",
      action: {
        arg buf;

        soundsOfSubConsciousSeas.buf_loaded(\splashingWaterBuf, buf);

      });
  
  });

  

}.value();)


